package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    /**
     * 抽象方法让子类实现
     *
     * @return
     */

    protected abstract BaseDao<T> getEntityDao();

    @Override
    public List<T> findAll() {
        return getEntityDao().findAll();
    }

    @Override
    public void insert(T t) {
        getEntityDao().insert(t);
    }

    @Override
    public T getById(Serializable id) {
        return getEntityDao().getById(id);
    }

    @Override
    public void update(T t) {
        getEntityDao().update(t);
    }

    @Override
    public void delete(Serializable id) {
        getEntityDao().delete(id);
    }

    @Override
    public PageInfo<T> findPage(Map<String, Object> filters) {
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 2);
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(getEntityDao().findPage(filters), 10);
    }
}
