package com.beeant.common.base;

import com.beeant.common.utils.DataTables;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Beeant on 2016/2/17.
 */
public interface IBaseController<Obj, Example> {

    String getBasePath();

    Example getExample();

    /**
     * get service
     *
     * @return
     */
    IBaseService<Obj, Example> getService();

    /**
     * create
     *
     * @param record obj
     * @param model  Model
     * @return basePath + "create"
     */
    ModelAndView create(Obj record, Model model);

    /**
     * delete
     *
     * @param key primary key
     * @return basePath + "delete"
     */
    ModelAndView delete(String key, Model model);

    /**
     * edit
     *
     * @param record obj
     * @param model  Model
     * @return basePath + "edit"
     */
    ModelAndView edit(@RequestParam String key, Obj record, Model model);

    /**
     * view
     *
     * @param key   primary key
     * @param model Model
     * @return basePath + "view"
     */
    ModelAndView view(String key, Model model);

    /**
     * browse
     *
     * @param record obj
     * @return basePath + "browse"
     */
    ModelAndView browse(Obj record, DataTables<Obj> pager);
}
