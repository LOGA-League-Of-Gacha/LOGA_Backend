package com.loga.global.error;

/**
 * 엔티티를 찾을 수 없을 때 발생하는 예외
 */
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static EntityNotFoundException of(ErrorCode errorCode, String entityName, String id) {
        return new EntityNotFoundException(errorCode,
                String.format("%s not found with id: %s", entityName, id));
    }
}
