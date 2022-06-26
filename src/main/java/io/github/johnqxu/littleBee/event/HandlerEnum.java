package io.github.johnqxu.littleBee.event;

public enum HandlerEnum {
    INIT_DB("init_db", "初始化数据库", 0.1),
    IMPORT_EMPLOY("import_employ", "导入员工信息", 0.2),
    IMPORT_PROJECTS("import_projects", "导入项目信息", 0.3),
    IMPORT_SIGNIN("import_signin", "导入签到信息", 0.4),
    VALIDATE_EMPLOY("validate_employ", "校验员工数据", 0.5),
    VALIDATE_PROJECT("validate_project", "校验课程数据", 0.6),
    VALIDATE_SIGNIN("validate_signin", "校验签到数据", 0.7),
    ASIGN("asign", "关联处理数据", 1);

    private String handler;
    private String handlerName;
    private double percentage;

    private HandlerEnum(String handler, String handlerName, double percentage) {
        this.handler = handler;
        this.handlerName = handlerName;
        this.percentage = percentage;
    }

    public static HandlerEnum getFirstHandler() {
        return INIT_DB;
    }

    public String getHandler() {
        return this.handler;
    }

    public String getHandlerName() {
        return this.handlerName;
    }

    public double getPercentage() {
        return this.percentage;
    }

}
