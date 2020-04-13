package com.xnew.kaleidoscope.dbgen.controller;

import com.xnew.kaleidoscope.dbgen.entity.Column;
import com.xnew.kaleidoscope.dbgen.entity.DBMsg;
import com.xnew.kaleidoscope.dbgen.entity.Response;
import com.xnew.kaleidoscope.dbgen.service.DBGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @ClassName DBGenController
 * @Description TODO
 * @Author 13503
 * @Date 2020/3/21 21:29
 * @Version 1.0
 **/
@Controller
@RequestMapping("/xnew/tools")
public class DBGenController {

    @Autowired
    private DBGenService dbGenService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(String databasename){
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }

    @RequestMapping(value = "/connect",method = RequestMethod.POST)
    @ResponseBody
    public Response connect(DBMsg dbMsg){
        return new Response(dbGenService.connectDB(dbMsg));
    }

    @RequestMapping(value = "/getAllTable",method = RequestMethod.GET)
    @ResponseBody
    public Response getAllTable(DBMsg dbMsg){
        return new Response(dbGenService.getDbAllTable(dbMsg));
    }

    @RequestMapping(value = "/getTableColumn",method = RequestMethod.GET)
    @ResponseBody
    public Response getTableColumn(DBMsg dbMsg){
        return new Response(dbGenService.getTableColumn(dbMsg));
    }

    @RequestMapping(value = "/genCode",method = RequestMethod.POST)
    @ResponseBody
    public Response genCode(DBMsg dbMsg){
        return new Response(dbGenService.genCode(dbMsg));
    }

    @RequestMapping(value = "/createTable",method = RequestMethod.POST)
    @ResponseBody
    public Response createTable(DBMsg dbMsg){
        return new Response(dbGenService.createTable(dbMsg));
    }

    @RequestMapping(value = "/addTableColumn",method = RequestMethod.POST)
    @ResponseBody
    public boolean addTableColumn(Column column){
        return dbGenService.addTableColumn(column);
    }

    @RequestMapping(value = "/addTableColumnBySql",method = RequestMethod.POST)
    @ResponseBody
    public Response addTableColumnBySql(String databaseName,String tableName,String sql){
        return new Response(dbGenService.addTableColumnBySql(databaseName,tableName,sql));
    }

    @RequestMapping(value = "/dropTableColumn",method = RequestMethod.POST)
    @ResponseBody
    public Response dropTableColumn(String databaseName,String tableName,String column){
        return new Response(dbGenService.dropTableColumn(databaseName,tableName,column));
    }

    @RequestMapping(value = "/dropTable",method = RequestMethod.POST)
    @ResponseBody
    public Response dropTable(String databaseName,String tableName){
        return new Response(dbGenService.dropTable(databaseName,tableName));
    }
}
