package com.admin.tool.common.model;

public enum Code {
    SUCCESS(0, "성공"), FAIL(1, "실패");

    public final String label;
    public final int code;

    Code(int code, String label) {
        this.code = code;
        this.label = label;
    }

}
