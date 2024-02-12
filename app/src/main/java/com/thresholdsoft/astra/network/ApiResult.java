package com.thresholdsoft.astra.network;

    public abstract class ApiResult<T> {
        public static class Success<T> extends ApiResult<T> {
            private final T value;

            public Success(T value) {
                this.value = value;
            }

            public T getValue() {
                return value;
            }
        }

        public static class GenericError extends ApiResult<Void> {
            private final Integer code;
            private final String error;
            private final String data;

            public GenericError(Integer code, String error, String data) {
                this.code = code;
                this.error = error;
                this.data = data;
            }

            public Integer getCode() {
                return code;
            }

            public String getError() {
                return error;
            }

            public String getData() {
                return data;
            }
        }

        public static class NetworkError extends ApiResult<Void> {}

        public static class UnknownError extends ApiResult<Void> {
            private final String message;

            public UnknownError(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }
        }

        public static class UnknownHostException extends ApiResult<Void> {
            private final String message;

            public UnknownHostException(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }
        }

        public String extractErrorString() {
            if (this instanceof Success) {
                return "No error";
            } else if (this instanceof GenericError) {
                return ((GenericError) this).getError() != null ? ((GenericError) this).getError() : "Api Error";
            } else if (this instanceof NetworkError) {
                return "Network Error";
            } else if (this instanceof UnknownError) {
                return "Something went wrong";
            } else if (this instanceof UnknownHostException) {
                return "Please Try again";
            }
            return "Unknown error";
        }
    }


