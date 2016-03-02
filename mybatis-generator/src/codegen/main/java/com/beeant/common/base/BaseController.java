package com.beeant.common.base;

import com.beeant.common.Message;
import com.beeant.common.utils.BeanUtils;
import com.beeant.common.utils.DataTables;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Beeant on 2016/2/17.
 */
@Controller
public abstract class BaseController<Obj, Example> implements IBaseController<Obj, Example> {
    /**
     * create
     *
     * @param record obj
     * @param model  Model
     * @return basePath + "create"
     */
    @RequestMapping("create")
    public ModelAndView create(Obj record, Model model) {
        ModelAndView mav = new ModelAndView(getBasePath().concat("create"));
        if(!BeanUtils.isNull(record)){
            Message<Integer> msg = getService().insert(record);
            mav.addObject(msg);
        }
        return mav;
    }

    /**
     * delete
     *
     * @param key primary key
     * @return basePath + "delete"
     */
    @RequestMapping("delete")
    public ModelAndView delete(@RequestParam String key, Model model) {
        ModelAndView mav = new ModelAndView(getBasePath().concat("delete"));
        Message<Integer> msg = getService().deleteByPrimaryKey(key);
        mav.addObject(msg);
        return mav;
    }

    /**
     * edit
     *
     * @param record obj
     * @param model  Model
     * @return basePath + "edit"
     */
    @RequestMapping("edit")
    public ModelAndView edit(@RequestParam String key, Obj record, Model model) {
        ModelAndView mav = new ModelAndView(getBasePath().concat("edit"));
        if(!BeanUtils.isNull(record)) {
            Message<Integer> msg = getService().updateByPrimaryKeySelective(record);
            mav.addObject(msg);
        } else {
            Message<Obj> msg = getService().selectByPrimaryKey(key);
            mav.addObject(msg);
        }
        return mav;
    }

    /**
     * view
     *
     * @param key   primary key
     * @param model Model
     * @return basePath + "view"
     */
    @RequestMapping("view")
    public ModelAndView view(@RequestParam String key, Model model) {
        ModelAndView mav = new ModelAndView(getBasePath().concat("view"));
        Message msg = getService().selectByPrimaryKey(key);
        mav.addObject(msg);
        return mav;
    }

    /**
     * browse
     *
     * @param record obj
     * @return basePath + "browse"
     */
    @RequestMapping("browse")
    public ModelAndView browse(Obj record, DataTables<Obj> pager) {
        RowBounds pageBounds = new RowBounds(pager.getStart(), pager.getLength());
        Message<List<Obj>> msg = getService().selectByConditionWithRowbounds(getExample(), pageBounds);
        if(msg.isSuccess()){
            pager.setData(msg.getData());
            PageInfo page = new PageInfo(msg.getData());
            page.setList(msg.getData());
            pager.setRecordsTotal(page.getTotal());
            pager.setRecordsFiltered(page.getTotal());
        }
        ModelAndView modelAndView = new ModelAndView(getBasePath().concat("browse"));
        //pager.setReturn(modelAndView);
        return modelAndView;
    }
}
