package com.itheima.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Description: 发布 创建节点数据 /dubbo/接口包名/providers/dubbo://ip:port 接口类 方法
 * client.create().creatingParentIfNeeded().for("dubbo/接口包名/providers/","数据");
 *
 * ServerSocket(20880);
 * socket = serverSocket.accept();
 * socket.getInputSTream() =?> findAll
 * List findAll();
 * socket.getOutputStream().write(List) 响应给调用者
 * User: Eric
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
      PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

      if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
          queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
      }

        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());

      PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(),page.getResult());

      return pageResult;


    }

    @Override
    public void deleteById(int id) throws HealthException {
        int cnt = checkItemDao.findCountByCheckItemId(id);

        if (cnt>0){
            throw new HealthException("该检查项被检查组使用了，不能删除");
        }

        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById( id);
    }

    @Override
    public void update(CheckItem checkItem) {
    checkItemDao.update( checkItem);
    }


}
