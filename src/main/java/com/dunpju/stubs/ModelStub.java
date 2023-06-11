package com.dunpju.stubs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ModelStub {

    private String outPackage;
    private final List<String> imports = new ArrayList<>();
    private String tableName;
    private final ApiModel apiModel = new ApiModel();

    @Data
    public static class ApiModel {
        private String value;
        private String description;
    }

    private String className;
    private String tablePrimaryKey;
    private String apiModelPropertyStub = "    @ApiModelProperty(\"%API_MODEL_PROPERTY%\")";
    private String tableIdStub = "    @TableId(value = \"%TABLE_PRIMARY_KEY%\", type = IdType.AUTO)";
    private String propertyStub = "    private %PROPERTY_TYPE% %PROPERTY_NAME%;";
    private final StringBuffer property = new StringBuffer();

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.annotation.IdType;
                import com.baomidou.mybatisplus.annotation.TableId;
                import com.baomidou.mybatisplus.annotation.TableName;
                                
                import java.io.Serial;
                import java.io.Serializable;
                import io.swagger.annotations.ApiModel;
                import io.swagger.annotations.ApiModelProperty;
                %IMPORTS%
                                
                @TableName("%TABLE_NAME%")
                @ApiModel(value = "%API_MODEL_VALUE%", description = "%API_MODEL_DESCRIPTION%")
                @Data
                public class %CLASS_NAME% implements Serializable {
                                
                    @Serial
                    private static final long serialVersionUID = 1L;
                                
                    %PROPERTY%
                    
                    @ApiModelProperty("%API_MODEL_PROPERTY%")
                    @TableId(value = "%TABLE_PRIMARY_KEY%", type = IdType.AUTO)
                    private %PROPERTY_TYPE% %PROPERTY_NAME%;
                                
                    @ApiModelProperty("%API_MODEL_PROPERTY%")
                    private String title;
                                
                    @ApiModelProperty("点击量")
                    private Integer clicknum;
                                
                    @ApiModelProperty("创建时间")
                    private LocalDateTime createTime;
                                
                    public Integer getNewsId() {
                        return newsId;
                    }
                                
                    public void setNewsId(Integer newsId) {
                        this.newsId = newsId;
                    }
                                
                    public String getTitle() {
                        return title;
                    }
                                
                    public void setTitle(String title) {
                        this.title = title;
                    }
                                
                    public Integer getClicknum() {
                        return clicknum;
                    }
                                
                    public void setClicknum(Integer clicknum) {
                        this.clicknum = clicknum;
                    }
                                
                    public LocalDateTime getCreateTime() {
                        return createTime;
                    }
                                
                    public void setCreateTime(LocalDateTime createTime) {
                        this.createTime = createTime;
                    }
                                
                    @Override
                    public String toString() {
                        return "%CLASS_NAME%{" +
                            "newsId = " + newsId +
                            ", title = " + title +
                            ", clicknum = " + clicknum +
                            ", createTime = " + createTime +
                        "}";
                    }
                }""";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%TABLE_NAME%", this.tableName);
        tpl = tpl.replaceAll("%API_MODEL_VALUE%", this.apiModel.getValue());
        tpl = tpl.replaceAll("%API_MODEL_DESCRIPTION%", this.apiModel.getDescription());
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }
}
