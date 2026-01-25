#!/bin/bash

set -e

# ========================================
# LOGA Backend Rollback Script
# ========================================

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

DEPLOY_DIR=${DEPLOY_DIR:-/home/ubuntu/loga}
cd $DEPLOY_DIR

# 롤백 버전 (기본값: 1 = 바로 이전 버전)
VERSION=${1:-1}

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}LOGA Backend Rollback${NC}"
echo -e "${BLUE}========================================${NC}"

# 배포 히스토리 확인
if [ ! -f .deploy_history ]; then
    echo -e "${RED}No deployment history found!${NC}"
    exit 1
fi

# 현재 배포된 버전 (히스토리의 마지막)
CURRENT_VERSION=$(tail -1 .deploy_history)

# 롤백 대상 버전 (VERSION 만큼 이전)
TOTAL_VERSIONS=$(wc -l < .deploy_history)
TARGET_LINE=$((TOTAL_VERSIONS - VERSION))

if [ $TARGET_LINE -lt 1 ]; then
    echo -e "${RED}Not enough deployment history for rollback version ${VERSION}${NC}"
    echo -e "${YELLOW}Available versions: $((TOTAL_VERSIONS - 1))${NC}"
    exit 1
fi

ROLLBACK_VERSION=$(sed -n "${TARGET_LINE}p" .deploy_history)

echo -e "${YELLOW}Current version: ${CURRENT_VERSION}${NC}"
echo -e "${YELLOW}Rolling back to: ${ROLLBACK_VERSION}${NC}"

# 환경 변수 로드
if [ -f .env.production ]; then
    export $(cat .env.production | grep -v '^#' | xargs)
fi

# GCP Artifact Registry 설정
GAR_LOCATION=${GAR_LOCATION:-asia-northeast3-docker.pkg.dev}
GCP_PROJECT_ID=${GCP_PROJECT_ID:-}
SERVICE_NAME=${SERVICE_NAME:-loga}

# 롤백 대상 이미지 확인 (로컬에 없으면 GCP Registry에서 pull)
if ! docker images loga-backend:${ROLLBACK_VERSION} --format "{{.Tag}}" | grep -q "${ROLLBACK_VERSION}"; then
    echo -e "${YELLOW}Local image not found: loga-backend:${ROLLBACK_VERSION}${NC}"

    # GCP Artifact Registry에서 pull 시도
    if [ -n "$GCP_PROJECT_ID" ] && [ -f /home/ubuntu/gcp-service-account.json ]; then
        echo -e "${BLUE}Attempting to pull from GCP Artifact Registry...${NC}"

        gcloud auth activate-service-account --key-file=/home/ubuntu/gcp-service-account.json --quiet
        gcloud auth configure-docker $GAR_LOCATION --quiet

        REMOTE_IMAGE="${GAR_LOCATION}/${GCP_PROJECT_ID}/${SERVICE_NAME}/loga-backend:${ROLLBACK_VERSION}"

        if docker pull "$REMOTE_IMAGE" 2>/dev/null; then
            echo -e "${GREEN}Successfully pulled from GCP Registry${NC}"
            docker tag "$REMOTE_IMAGE" loga-backend:${ROLLBACK_VERSION}
        else
            echo -e "${RED}Failed to pull from GCP Registry: ${REMOTE_IMAGE}${NC}"
            echo -e "${YELLOW}Available local images:${NC}"
            docker images loga-backend --format "table {{.Tag}}\t{{.CreatedAt}}"
            exit 1
        fi
    else
        echo -e "${RED}Rollback image not found locally and GCP credentials not configured${NC}"
        echo -e "${YELLOW}Available local images:${NC}"
        docker images loga-backend --format "table {{.Tag}}\t{{.CreatedAt}}"
        exit 1
    fi
fi

# 현재 활성 컨테이너 확인
echo -e "${BLUE}Checking current active container...${NC}"
if curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; then
    CURRENT_CONTAINER="blue"
    NEW_CONTAINER="green"
    CURRENT_PORT=8080
    NEW_PORT=8081
    echo -e "${GREEN}Current: Blue (port 8080)${NC}"
else
    CURRENT_CONTAINER="green"
    NEW_CONTAINER="blue"
    CURRENT_PORT=8081
    NEW_PORT=8080
    echo -e "${GREEN}Current: Green (port 8081)${NC}"
fi

# 롤백 이미지로 태그 변경
echo -e "${BLUE}Tagging rollback image as latest...${NC}"
docker tag loga-backend:${ROLLBACK_VERSION} loga-backend:latest

# 새 컨테이너로 롤백 배포
echo -e "${BLUE}Deploying rollback version to ${NEW_CONTAINER}...${NC}"
docker compose up -d --no-deps ${NEW_CONTAINER}

echo -e "${YELLOW}Waiting for ${NEW_CONTAINER} to start (60 seconds)...${NC}"
sleep 60

# 헬스체크
echo -e "${BLUE}Health checking rollback deployment...${NC}"
HEALTHY=false

for i in {1..30}; do
    if curl -sf http://localhost:${NEW_PORT}/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}Rollback deployment healthy!${NC}"
        HEALTHY=true
        break
    fi
    echo -e "${YELLOW}Waiting for rollback to be ready... ($i/30)${NC}"
    sleep 2
done

if [ "$HEALTHY" = true ]; then
    echo -e "${BLUE}Switching traffic to rollback version...${NC}"

    # Nginx 설정 업데이트
    if [ -f /etc/nginx/sites-available/loga ]; then
        sudo sed -i "s/localhost:[0-9]\{4\}/localhost:${NEW_PORT}/" /etc/nginx/sites-available/loga
        sudo nginx -t && sudo systemctl reload nginx
    fi

    sleep 5

    docker compose stop ${CURRENT_CONTAINER}

    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Rollback completed successfully!${NC}"
    echo -e "${GREEN}Rolled back to: ${ROLLBACK_VERSION}${NC}"
    echo -e "${GREEN}Active Container: ${NEW_CONTAINER} (port ${NEW_PORT})${NC}"
    echo -e "${GREEN}========================================${NC}"

    exit 0
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}Rollback failed health check!${NC}"
    echo -e "${RED}Keeping current version: ${CURRENT_CONTAINER}${NC}"
    echo -e "${RED}========================================${NC}"

    docker compose stop ${NEW_CONTAINER}
    docker compose start ${CURRENT_CONTAINER}

    exit 1
fi
