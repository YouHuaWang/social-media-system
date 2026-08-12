CREATE OR REPLACE PROCEDURE SP_REGISTER (
    P_USER_NAME IN USERS.USER_NAME%TYPE,
    P_PHONE IN USERS.PHONE%TYPE,
    P_EMAIL IN USERS.EMAIL%TYPE,
    P_PASSWORD_HASH IN USERS.PASSWORD_HASH%TYPE,
    P_USER_ID OUT USERS.USER_ID%TYPE,
    P_RESULT OUT NUMBER
)
AS
    V_COUNT NUMBER;
BEGIN
    -- Check if the phone number is already registered
    SELECT COUNT(*)
    INTO V_COUNT
    FROM USERS
    WHERE PHONE = P_PHONE;

    IF V_COUNT > 0 THEN
        P_USER_ID := NULL;
        P_RESULT := 0;
        RETURN;
    END IF;
    
    -- Check if email is alreay registered
    IF P_EMAIL IS NOT NULL THEN
        SELECT COUNT(*)
        INTO V_COUNT
        FROM USERS
        WHERE EMAIL = P_EMAIL;

        IF V_COUNT > 0 THEN
            P_USER_ID := NULL;
            P_RESULT  := -2;
            RETURN;
        END IF;
    END IF;

    INSERT INTO USERS (
        USER_NAME,
        PHONE,
        EMAIL,
        PASSWORD_HASH,
        PHONE_VERIFIED,
        EMAIL_VERIFIED,
        CREATED_AT,
        UPDATED_AT
    ) VALUES (
        P_USER_NAME,
        P_PHONE,
        P_EMAIL,
        P_PASSWORD_HASH,
        0,
        0,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    )
    RETURNING USER_ID INTO P_USER_ID;

    P_RESULT := 1; -- registration successful

EXCEPTION
    WHEN OTHERS THEN
        P_RESULT := -1;
        RAISE;
END SP_REGISTER;
/