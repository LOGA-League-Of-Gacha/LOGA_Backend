#!/bin/bash

set -e

# ========================================
# LOGA Backend Blue-Green Deployment
# ========================================

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 환경 변수 로드
if [ -f .env.production ]; then
    export $(cat .env.production | grep -v '^#' | xargs)
fi

# 이미지 태그 (GitHub SHA 또는 timestamp)
IMAGE_TAG=${1:-$(date +%Y%m%d_%H%M%S)}

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Starting Deployment: ${IMAGE_TAG}${NC}"
echo -e "${BLUE}========================================${NC}"

DEPLOY_DIR=${DEPLOY_DIR:-/home/ubuntu/loga}
cd $DEPLOY_DIR

# 이전 이미지 태그 저장 (롤백용)
LAST_IMAGE=$(docker images loga-backend --format "{{.Tag}}" | head -n 1)
echo "$LAST_IMAGE" > $DEPLOY_DIR/.last_deploy
echo -e "${YELLOW}Previous image tag saved: ${LAST_IMAGE}${NC}"

# 배포 히스토리 저장 (최근 10개 유지)
echo "$IMAGE_TAG" >> $DEPLOY_DIR/.deploy_history
tail -10 $DEPLOY_DIR/.deploy_history > $DEPLOY_DIR/.deploy_history.tmp
mv $DEPLOY_DIR/.deploy_history.tmp $DEPLOY_DIR/.deploy_history
echo -e "${YELLOW}Deployment history updated${NC}"

echo -e "${BLUE}Using Docker image: loga-backend:latest${NC}"

# 현재 활성 컨테이너 확인 (헬스체크 기반)
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

echo -e "${BLUE}Deploying to ${NEW_CONTAINER} container...${NC}"

# 새 컨테이너 배포
docker compose up -d --no-deps --build ${NEW_CONTAINER}

echo -e "${YELLOW}Waiting for ${NEW_CONTAINER} to start (60 seconds for Spring Boot)...${NC}"
sleep 60

# 헬스체크
echo -e "${BLUE}Health checking ${NEW_CONTAINER} deployment...${NC}"
HEALTHY=false

for i in {1..30}; do
    if curl -sf http://localhost:${NEW_PORT}/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}${NEW_CONTAINER} deployment healthy!${NC}"
        HEALTHY=true
        break
    fi
    echo -e "${YELLOW}Waiting for ${NEW_CONTAINER} to be ready... ($i/30)${NC}"
    sleep 2
done

if [ "$HEALTHY" = true ]; then
    echo -e "${BLUE}Switching traffic to ${NEW_CONTAINER}...${NC}"

    # Nginx 설정 업데이트 (upstream을 새 포트로 변경)
    if [ -f /etc/nginx/sites-available/loga ]; then
        echo -e "${BLUE}Updating Nginx configuration...${NC}"
        # Nginx에서 upstream 포트를 새 포트로 변경
        sudo sed -i "s/localhost:[0-9]\{4\}/localhost:${NEW_PORT}/" /etc/nginx/sites-available/loga
        sudo nginx -t && sudo systemctl reload nginx
        echo -e "${GREEN}Nginx reloaded with new upstream${NC}"
    fi

    sleep 5

    echo -e "${BLUE}Stopping old ${CURRENT_CONTAINER} container...${NC}"
    docker compose stop ${CURRENT_CONTAINER}

    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Deployment completed successfully!${NC}"
    echo -e "${GREEN}Active Container: ${NEW_CONTAINER} (port ${NEW_PORT})${NC}"
    echo -e "${GREEN}========================================${NC}"

    exit 0
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}${NEW_CONTAINER} deployment failed health check!${NC}"
    echo -e "${RED}Rolling back to ${CURRENT_CONTAINER}...${NC}"
    echo -e "${RED}========================================${NC}"

    docker compose stop ${NEW_CONTAINER}
    docker compose start ${CURRENT_CONTAINER}

    exit 1
fi
