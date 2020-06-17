package com.example.test;

import java.util.regex.Pattern;

/**
 * @author chengsp
 * @date 2020/6/9 17:15
 */
public class Colum {


    private static final Pattern P = Pattern.compile("null", Pattern.CASE_INSENSITIVE);
    private static final Pattern P2 = Pattern.compile("true", Pattern.CASE_INSENSITIVE);
    private String code;
    private String comment;
    private String type;
    private String defaultValue;
    private String primary;

    public Colum(String code, String comment, String type, String defaultValue, String primary) {
        this.code = code;
        this.comment = comment;
        this.type = type;
        this.defaultValue = defaultValue;
        this.primary = primary;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(" ").append(type).append(" ");

        boolean b = primary != null && P2.matcher(primary).find();
        boolean b1 = P.matcher(defaultValue).find();
        if ((defaultValue == null || defaultValue.length() == 0 || b1 && !b)) {
            sb.append("null ");
        } else {
            if (!code.equalsIgnoreCase("id") && !b1) {
                sb.append(" default '").append(defaultValue).append("'");
            }
            sb.append("not null ");

        }
        sb.append("comment ").append("\'").append(comment).append("'\n");
        if (b && code.equalsIgnoreCase("id")) {
            sb.append("primary key ");
        }
        return sb.toString();
    }
}
