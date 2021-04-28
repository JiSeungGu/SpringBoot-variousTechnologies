package net.sejongtelecom.backendapi.common.define;

import org.springframework.stereotype.Service;

@Service
public class TypeDefine {
    public enum EncDecCode {
        ENCRYPT_MODE(2000, "ENCRYPT_MODE"),
        DECRYPT_MODE(2001, "DECRYPT_MODE");

        private final int code;
        private final String message;

        EncDecCode(final int code, final String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode(){
            return code;
        }

        public String getMessage(){
            return message;
        }
    }
}
