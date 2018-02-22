package org.georchestra;

public class Response {

    private int responseCode;
    private String contentType;
    private String response;

    public Response(int responseCode, String contentType, String response) {
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
