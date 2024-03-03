package com.gt.facerecognition.model.domain;

public class User {

    private int code;
    private String message;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int id;
        private String user_account_phone;
        private String user_name;
        private String password;
        private String gender;
        private int ret;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_account_phone() {
            return user_account_phone;
        }

        public void setUser_account_phone(String user_account_phone) {
            this.user_account_phone = user_account_phone;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        @Override
        public String toString() {
            return "DataDTO{" +
                    "id=" + id +
                    ", user_account_phone='" + user_account_phone + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", password='" + password + '\'' +
                    ", gender='" + gender + '\'' +
                    ", ret=" + ret +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
