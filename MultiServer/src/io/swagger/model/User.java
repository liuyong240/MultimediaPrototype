package io.swagger.model;


//import com.wordnik.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModel;

/**
 * Created by yangdongxu on 15/11/5.
 */

@ApiModel(value="User")
public class User {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
