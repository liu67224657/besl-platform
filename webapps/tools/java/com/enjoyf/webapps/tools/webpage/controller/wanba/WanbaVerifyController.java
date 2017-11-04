package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.Verify;
import com.enjoyf.platform.service.usercenter.VerifyField;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/26
 */
@Controller
@RequestMapping("/wanba/verify")
public class WanbaVerifyController {

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "text", required = false) String text,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();

        try {
            PageRows<Verify> wanbaVerifyPageRows = UserCenterServiceSngl.get().queryVerifyByPage(queryExpress, page);
            mapMessage.put("list", wanbaVerifyPageRows.getRows());
            mapMessage.put("page", wanbaVerifyPageRows.getPage());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
        }

        return new ModelAndView("/wanba/verify/list", mapMessage);
    }


    @RequestMapping("/createpage")
    public ModelAndView createpage() {
        return new ModelAndView("/wanba/verify/createpage");
    }

    //////////认证用户//////////////
    @RequestMapping("/create")
    public ModelAndView verifyProfile(@RequestParam(value = "verifyname", required = false) String verifyName) {
        try {
            Verify wanbaVerify = new Verify();
            wanbaVerify.setCreateDate(new Date());
            wanbaVerify.setValidStatus(ValidStatus.VALID);
            wanbaVerify.setVerifyName(verifyName);
            UserCenterServiceSngl.get().addVerify(wanbaVerify);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/verify/list");
    }

    @RequestMapping("/remove")
    public ModelAndView removeInfo() {
        return null;
    }

    @RequestMapping("/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "verifyid", required = false) String verifyId) {
        Map<String, Object> messageMap = new HashMap<String, Object>();
        try {
            Verify wanbaVerify = UserCenterServiceSngl.get().getVerify(Long.parseLong(verifyId));
            messageMap.put("verify", wanbaVerify);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/verify/modifypage", messageMap);
    }

    @RequestMapping("/modify")
    public ModelAndView modify(@RequestParam(value = "verifyid", required = false) String verifyId,
                               @RequestParam(value = "verifyname", required = false) String verifyName) {

        try {
            UserCenterServiceSngl.get().modifyVerify(Long.parseLong(verifyId), new UpdateExpress().set(VerifyField.VERIFY_NAME, verifyName));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/verify/list");
    }

    ///////////////达人列表//////////////////////


    ///////////////认证类型//////////////////
}
