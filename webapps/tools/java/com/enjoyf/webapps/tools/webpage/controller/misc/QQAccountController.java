package com.enjoyf.webapps.tools.webpage.controller.misc;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.InterFlowAccount;
import com.enjoyf.platform.service.misc.InterFlowAccountField;
import com.enjoyf.platform.service.misc.InterFlowType;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 下午12:18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/misc/qq")
public class QQAccountController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             @RequestParam(value = "qid", required = false) String qid,
                             @RequestParam(value = "qac", required = false) String qac,
                             @RequestParam(value = "gname", required = false) String gname,
                             @RequestParam(value = "qunum", required = false) String qunum,
                             @RequestParam(value = "qlevel", required = false) String qlevel,
                             @RequestParam(value = "qmanu", required = false) String qmanu,
                             @RequestParam(value = "gcate", required = false) String gcate,
                             @RequestParam(value = "gtype", required = false) String gtype,
                             @RequestParam(value = "qplat", required = false) String qplat,
                             @RequestParam(value = "qtheme", required = false) String qtheme,
                             @RequestParam(value = "qduty", required = false) String qduty,
                             @RequestParam(value = "qarea", required = false) String qarea,
                             @RequestParam(value = "qstatus", required = false) String qstatus
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);

        mapMessage.put("qid", qid);
        mapMessage.put("qac", qac);
        mapMessage.put("qunum", qunum);
        mapMessage.put("qlevel", qlevel);
        mapMessage.put("gname", gname);
        mapMessage.put("qduty", qduty);
        mapMessage.put("qtheme", qtheme);
        mapMessage.put("gtype", gtype);
        mapMessage.put("gcate", gcate);
        mapMessage.put("qplat", qplat);
        mapMessage.put("qmanu", qmanu);
        mapMessage.put("qstatus", qstatus);
        mapMessage.put("qarea", qarea);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(qid)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, qid));
        }
        if (!StringUtil.isEmpty(qac)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ACCOUNT, qac));
        }
        if (!StringUtil.isEmpty(qunum)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.USER_NUMBER, qunum));
        }
        if (!StringUtil.isEmpty(qlevel)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.LEVEL, qlevel));
        }
        if (!StringUtil.isEmpty(gname)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_NAME, gname));
        }
        if (!StringUtil.isEmpty(qduty)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.DUTY_USER, qduty));
        }
        if (!StringUtil.isEmpty(qtheme)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.THEME, qtheme));
        }
        if (!StringUtil.isEmpty(gtype)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_TYPE, gtype));
        }
        if (!StringUtil.isEmpty(gcate)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_CATEGORY, gcate));
        }
        if (!StringUtil.isEmpty(qplat)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.PLATFORM, qplat));
        }

        if (!StringUtil.isEmpty(qmanu)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.MANUFACTURER, qmanu));
        }

        if (!StringUtil.isEmpty(qstatus)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.REMOVE_STATUS, qstatus));
        } else {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        }

        if (!StringUtil.isEmpty(qarea)) {
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.PUBLISH_AREA, qarea));
        }
        queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_TYPE, InterFlowType.QQ.getCode()));

        int curPage = pageStartIndex / pageSize + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            PageRows<InterFlowAccount> pageRows = MiscServiceSngl.get().queryInterFlowAccountByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/misc/interflow/qqlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "ifname", required = false) String ifName,
                                   @RequestParam(value = "ifaccount", required = false) String ifAccount,
                                   @RequestParam(value = "iflord", required = false) String ifLord,
                                   @RequestParam(value = "ifduty", required = false) String ifDuty,
                                   @RequestParam(value = "ifusernumber", required = false) String ifUserNumber,
                                   @RequestParam(value = "iflevel", required = false) String ifLevel,
                                   @RequestParam(value = "ifmanufacturer", required = false) String ifManufacturer,
                                   @RequestParam(value = "ifgamename", required = false) String ifGameName,
                                   @RequestParam(value = "ifgamecategory", required = false) String ifGameCategory,
                                   @RequestParam(value = "ifgametype", required = false) String ifGameType,
                                   @RequestParam(value = "ifplatform", required = false) String ifPlatform,
                                   @RequestParam(value = "iftheme", required = false) String ifTheme,
                                   @RequestParam(value = "ifarea", required = false) String ifArea
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("ifName", ifName);
        mapMessage.put("ifAccount", ifAccount);
        mapMessage.put("ifLord", ifLord);
        mapMessage.put("ifDuty", ifDuty);
        mapMessage.put("ifUserNumber", ifUserNumber);
        mapMessage.put("ifLevel", ifLevel);
        mapMessage.put("ifManufacturer", ifManufacturer);
        mapMessage.put("ifGameName", ifGameName);
        mapMessage.put("ifGameCategory", ifGameCategory);
        mapMessage.put("ifGameType", ifGameType);
        mapMessage.put("ifPlatform", ifPlatform);
        mapMessage.put("ifTheme", ifTheme);
        mapMessage.put("ifArea", ifArea);

        try {
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/misc/interflow/createqq", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "ifname", required = false) String ifName,
                               @RequestParam(value = "ifaccount", required = false) String ifAccount,
                               @RequestParam(value = "iflord", required = false) String ifLord,
                               @RequestParam(value = "ifduty", required = false) String ifDuty,
                               @RequestParam(value = "ifusernumber", required = false) String ifUserNumber,
                               @RequestParam(value = "iflevel", required = false) String ifLevel,
                               @RequestParam(value = "ifmanufacturer", required = false) String ifManufacturer,
                               @RequestParam(value = "ifgamename", required = false) String ifGameName,
                               @RequestParam(value = "ifgamecategory", required = false) String ifGameCategory,
                               @RequestParam(value = "ifgametype", required = false) String ifGameType,
                               @RequestParam(value = "ifplatform", required = false) String ifPlatform,
                               @RequestParam(value = "iftheme", required = false) String ifTheme,
                               @RequestParam(value = "ifarea", required = false) String ifArea
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("ifname", ifName);
        mapMessage.put("ifaccount", ifAccount);
        mapMessage.put("iflord", ifLord);
        mapMessage.put("ifduty", ifDuty);
        mapMessage.put("ifusernumber", ifUserNumber);
        mapMessage.put("iflevel", ifLevel);
        mapMessage.put("ifmanufacturer", ifManufacturer);
        mapMessage.put("ifgamename", ifGameName);
        mapMessage.put("ifgamecategory", ifGameCategory);
        mapMessage.put("ifgametype", ifGameType);
        mapMessage.put("ifplatform", ifPlatform);
        mapMessage.put("iftheme", ifTheme);
        mapMessage.put("ifarea", ifArea);

        try {
            InterFlowAccount interFlowAccount = new InterFlowAccount();
            interFlowAccount.setName(ifName);
            interFlowAccount.setAccount(ifAccount);
            interFlowAccount.setLord(ifLord);
            interFlowAccount.setType(InterFlowType.QQ);
            interFlowAccount.setDuty(ifDuty);
            interFlowAccount.setUserNumber(ifUserNumber);
            interFlowAccount.setLevel(ifLevel);
            interFlowAccount.setManufacturer(ifManufacturer);
            interFlowAccount.setGameName(ifGameName);
            interFlowAccount.setGameCategory(ifGameCategory);
            interFlowAccount.setGameType(ifGameType);
            interFlowAccount.setPlatform(ifPlatform);
            interFlowAccount.setTheme(ifTheme);
            interFlowAccount.setPublishArea(ifArea);
            interFlowAccount.setCreateDate(new Date());
            interFlowAccount.setCreateUser(getCurrentUser().getUserid());
            interFlowAccount.setRemoveStatus(ValidStatus.VALID);

            MiscServiceSngl.get().createInterFlowAccount(interFlowAccount);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/misc/qq/createpage", mapMessage);
        }
        return new ModelAndView("redirect:/misc/qq/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "ifid", required = false) String ifId
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, ifId));

        try {
            InterFlowAccount interFlowAccount = MiscServiceSngl.get().getInterFlowAccount(queryExpress);
            if (!getCurrentUser().getUserid().equals(interFlowAccount.getCreateUser())) {
                return new ModelAndView("redirect:/misc/qq/list?error=interflow.user.no.privilege");
            }
            if (interFlowAccount != null) {
                mapMessage.put("interFlow", interFlowAccount);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/misc/interflow/modifyqq", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "ifid", required = false) String ifId,
                               @RequestParam(value = "ifname", required = false) String ifName,
                               @RequestParam(value = "ifaccount", required = false) String ifAccount,
                               @RequestParam(value = "iflord", required = false) String ifLord,
                               @RequestParam(value = "ifduty", required = false) String ifDuty,
                               @RequestParam(value = "ifusernumber", required = false) String ifUserNumber,
                               @RequestParam(value = "iflevel", required = false) String ifLevel,
                               @RequestParam(value = "ifmanufacturer", required = false) String ifManufacturer,
                               @RequestParam(value = "ifgamename", required = false) String ifGameName,
                               @RequestParam(value = "ifgamecategory", required = false) String ifGameCategory,
                               @RequestParam(value = "ifgametype", required = false) String ifGameType,
                               @RequestParam(value = "ifplatform", required = false) String ifPlatform,
                               @RequestParam(value = "iftheme", required = false) String ifTheme,
                               @RequestParam(value = "ifarea", required = false) String ifArea
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("ifid", ifId);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(InterFlowAccountField.INTERFLOW_NAME, ifName);
        updateExpress.set(InterFlowAccountField.INTERFLOW_ACCOUNT, ifAccount);
        updateExpress.set(InterFlowAccountField.LORD_USER, ifLord);
        updateExpress.set(InterFlowAccountField.DUTY_USER, ifDuty);
        updateExpress.set(InterFlowAccountField.USER_NUMBER, ifUserNumber);
        updateExpress.set(InterFlowAccountField.LEVEL, ifLevel);
        updateExpress.set(InterFlowAccountField.MANUFACTURER, ifManufacturer);
        updateExpress.set(InterFlowAccountField.GAME_NAME, ifGameName);
        updateExpress.set(InterFlowAccountField.GAME_CATEGORY, ifGameCategory);
        updateExpress.set(InterFlowAccountField.GAME_TYPE, ifGameType);
        updateExpress.set(InterFlowAccountField.PLATFORM, ifPlatform);
        updateExpress.set(InterFlowAccountField.THEME, ifTheme);
        updateExpress.set(InterFlowAccountField.PUBLISH_AREA, ifArea);
        updateExpress.set(InterFlowAccountField.MODIFY_DATE, new Date());
        updateExpress.set(InterFlowAccountField.MODIFY_USERID, getCurrentUser().getUserid());

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, ifId));

        try {
            InterFlowAccount interFlowAccount = MiscServiceSngl.get().getInterFlowAccount(queryExpress);
            if (!getCurrentUser().getUserid().equals(interFlowAccount.getCreateUser())) {
                return new ModelAndView("redirect:/misc/qq/list?error=interflow.user.no.privilege");
            }

            MiscServiceSngl.get().modifyInterFlowAccount(updateExpress, queryExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/misc/qq/modifypage?ifid=" + ifId, mapMessage);
        }
        return new ModelAndView("redirect:/misc/qq/list");
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "ifid", required = false) String ifId,
                               @RequestParam(value = "pager.offset", required = false) int pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false) int pageSize,
                               @RequestParam(value = "qid", required = false) String qid,
                               @RequestParam(value = "qac", required = false) String qac,
                               @RequestParam(value = "gname", required = false) String gname,
                               @RequestParam(value = "qunum", required = false) String qunum,
                               @RequestParam(value = "qlevel", required = false) String qlevel,
                               @RequestParam(value = "qmanu", required = false) String qmanu,
                               @RequestParam(value = "gcate", required = false) String gcate,
                               @RequestParam(value = "gtype", required = false) String gtype,
                               @RequestParam(value = "qplat", required = false) String qplat,
                               @RequestParam(value = "qtheme", required = false) String qtheme,
                               @RequestParam(value = "qduty", required = false) String qduty,
                               @RequestParam(value = "qarea", required = false) String qarea,
                               @RequestParam(value = "qstatus", required = false) String qstatus
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("pager.offset", pageStartIndex);
        mapMessage.put("maxPageItems", pageSize);

        mapMessage.put("qid", qid);
        mapMessage.put("qac", qac);
        mapMessage.put("qunum", qunum);
        mapMessage.put("qlevel", qlevel);
        mapMessage.put("gname", gname);
        mapMessage.put("qduty", qduty);
        mapMessage.put("qtheme", qtheme);
        mapMessage.put("gtype", gtype);
        mapMessage.put("gcate", gcate);
        mapMessage.put("qplat", qplat);
        mapMessage.put("qmanu", qmanu);
        mapMessage.put("qstatus", qstatus);
        mapMessage.put("qarea", qarea);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, ifId));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(InterFlowAccountField.REMOVE_STATUS, ValidStatus.REMOVED.getCode());

        try {
            InterFlowAccount interFlowAccount = MiscServiceSngl.get().getInterFlowAccount(queryExpress);
            if (!getCurrentUser().getUserid().equals(interFlowAccount.getCreateUser())) {
                return new ModelAndView("redirect:/misc/qq/list?error=interflow.user.no.privilege");
            }

            MiscServiceSngl.get().modifyInterFlowAccount(updateExpress, queryExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/misc/qq/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "ifid", required = false) String ifId,
                                @RequestParam(value = "pager.offset", required = false) int pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false) int pageSize,
                                @RequestParam(value = "qid", required = false) String qid,
                                @RequestParam(value = "qac", required = false) String qac,
                                @RequestParam(value = "gname", required = false) String gname,
                                @RequestParam(value = "qunum", required = false) String qunum,
                                @RequestParam(value = "qlevel", required = false) String qlevel,
                                @RequestParam(value = "qmanu", required = false) String qmanu,
                                @RequestParam(value = "gcate", required = false) String gcate,
                                @RequestParam(value = "gtype", required = false) String gtype,
                                @RequestParam(value = "qplat", required = false) String qplat,
                                @RequestParam(value = "qtheme", required = false) String qtheme,
                                @RequestParam(value = "qduty", required = false) String qduty,
                                @RequestParam(value = "qarea", required = false) String qarea,
                                @RequestParam(value = "qstatus", required = false) String qstatus
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("pager.offset", pageStartIndex);
        mapMessage.put("maxPageItems", pageSize);

        mapMessage.put("qid", qid);
        mapMessage.put("qac", qac);
        mapMessage.put("qunum", qunum);
        mapMessage.put("qlevel", qlevel);
        mapMessage.put("gname", gname);
        mapMessage.put("qduty", qduty);
        mapMessage.put("qtheme", qtheme);
        mapMessage.put("gtype", gtype);
        mapMessage.put("gcate", gcate);
        mapMessage.put("qplat", qplat);
        mapMessage.put("qmanu", qmanu);
        mapMessage.put("qstatus", qstatus);
        mapMessage.put("qarea", qarea);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, ifId));

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(InterFlowAccountField.REMOVE_STATUS, ValidStatus.VALID.getCode());

        try {
            InterFlowAccount interFlowAccount = MiscServiceSngl.get().getInterFlowAccount(queryExpress);
            if (!getCurrentUser().getUserid().equals(interFlowAccount.getCreateUser())) {
                return new ModelAndView("redirect:/misc/qq/list?error=interflow.user.no.privilege");
            }

            MiscServiceSngl.get().modifyInterFlowAccount(updateExpress, queryExpress);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/misc/qq/list", mapMessage);
    }

    @RequestMapping(value = "/input")
    public ModelAndView input(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "Filedata", required = false) MultipartFile filedata
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        Workbook workbook = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            //读取文件
            InputStream in = filedata.getInputStream();
            if (in == null) {
                mapMessage = putErrorMessage(mapMessage, "file.not.exist");
                return new ModelAndView("forward:/misc/qq/list", mapMessage);
            }
            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.getWorkbook(in, workbookSettings);
            //表单
            Sheet st = workbook.getSheet(0);
            int columns = st.getColumns();
            int rows = st.getRows();
            for (int j = 1; j < rows; j++) {//行
                InterFlowAccount interFlowAccount = new InterFlowAccount();

                interFlowAccount.setCreateDate(new Date());
                interFlowAccount.setCreateUser(getCurrentUser().getUserid());
                interFlowAccount.setRemoveStatus(ValidStatus.VALID);
                interFlowAccount.setType(InterFlowType.QQ);

                for (int i = 1; i < columns; i++) {//列
                    //j行i列  的   单元格
                    Cell cell = st.getCell(i, j);
                    //通用的获取cell值的方式,返回字符串
                    String content = cell.getContents();
                    //获得cell具体类型值的方式
                    if (cell.getType() == CellType.LABEL) {
                        LabelCell labelCell = (LabelCell) cell;
                        content = labelCell.getString();
                    }
                    //excel 类型为时间类型处理;
                    if (cell.getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) cell;
                        content = sdf.format(dateCell.getDate());

                    }
                    //excel 类型为数值类型处理;
//                    if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
//                        NumberCell numberCell = (NumberCell) cell;
//                        content = String.valueOf(numberCell.getValue());
//                    }
                    //每一列对应的 字段 （双方约定规范，不得随意更改）
                    if (i == 1) {
                        interFlowAccount.setAccount(content);
                    } else if (i == 2) {
                        interFlowAccount.setName(content);
                    } else if (i == 3) {
                        interFlowAccount.setGameName(content);
                    } else if (i == 4) {
                        interFlowAccount.setUserNumber(content);
                    } else if (i == 5) {
                        interFlowAccount.setLevel(content);
                    } else if (i == 6) {
                        interFlowAccount.setDuty(content);
                    } else if (i == 7) {
                        interFlowAccount.setLord(content);
                    } else if (i == 8) {
                        interFlowAccount.setManufacturer(content);
                    } else if (i == 9) {
                        interFlowAccount.setGameCategory(content);
                    } else if (i == 10) {
                        interFlowAccount.setGameType(content);
                    } else if (i == 11) {
                        interFlowAccount.setPlatform(content);
                    } else if (i == 12) {
                        interFlowAccount.setTheme(content);
                    } else if (i == 13) {
                        interFlowAccount.setPublishArea(content);
                    } else if (i == 14) {
                        interFlowAccount.setLastPostDate(content);
                    }
                }
                InterFlowAccount interFlowAccount1 = MiscServiceSngl.get().getInterFlowAccount(new QueryExpress()
                        .add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, MD5Util.Md5(interFlowAccount.getAccount() + "-" + interFlowAccount.getType().getCode()))));
                if (interFlowAccount1 == null) {
                    MiscServiceSngl.get().createInterFlowAccount(interFlowAccount);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } finally {
            //关闭
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        return new ModelAndView("forward:/misc/qq/list", mapMessage);
    }

    @RequestMapping(value = "/output")
    public ModelAndView output(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "qid", required = false) String qid,
                               @RequestParam(value = "qac", required = false) String qac,
                               @RequestParam(value = "gname", required = false) String gname,
                               @RequestParam(value = "qunum", required = false) String qunum,
                               @RequestParam(value = "qlevel", required = false) String qlevel,
                               @RequestParam(value = "qmanu", required = false) String qmanu,
                               @RequestParam(value = "gcate", required = false) String gcate,
                               @RequestParam(value = "gtype", required = false) String gtype,
                               @RequestParam(value = "qplat", required = false) String qplat,
                               @RequestParam(value = "qtheme", required = false) String qtheme,
                               @RequestParam(value = "qduty", required = false) String qduty,
                               @RequestParam(value = "qarea", required = false) String qarea,
                               @RequestParam(value = "qstatus", required = false) String qstatus
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        WritableWorkbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            response.reset();

            OutputStream os = response.getOutputStream();

            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String("贴吧\\/QQ群帐号.xls".getBytes("GB2312"), "iso-8859-1"));

            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.createWorkbook(os, workbookSettings);
            WritableSheet writableSheet = workbook.createSheet("Sheet1", 0);

            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(qid)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ID, qid));
            }
            if (!StringUtil.isEmpty(qac)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_ACCOUNT, qac));
            }
            if (!StringUtil.isEmpty(qunum)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.USER_NUMBER, qunum));
            }
            if (!StringUtil.isEmpty(qlevel)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.LEVEL, qlevel));
            }
            if (!StringUtil.isEmpty(gname)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_NAME, gname));
            }
            if (!StringUtil.isEmpty(qduty)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.DUTY_USER, qduty));
            }
            if (!StringUtil.isEmpty(qtheme)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.THEME, qtheme));
            }
            if (!StringUtil.isEmpty(gtype)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_TYPE, gtype));
            }
            if (!StringUtil.isEmpty(gcate)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.GAME_CATEGORY, gcate));
            }
            if (!StringUtil.isEmpty(qplat)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.PLATFORM, qplat));
            }

            if (!StringUtil.isEmpty(qmanu)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.MANUFACTURER, qmanu));
            }
            if (!StringUtil.isEmpty(qstatus)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.REMOVE_STATUS, qstatus));
            } else {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            }
            if (!StringUtil.isEmpty(qarea)) {
                queryExpress.add(QueryCriterions.eq(InterFlowAccountField.PUBLISH_AREA, qarea));
            }
            queryExpress.add(QueryCriterions.eq(InterFlowAccountField.INTERFLOW_TYPE, InterFlowType.QQ.getCode()));

            Pagination page = null;
            int row = 1;

            do {
                page = new Pagination(100, 1, 100);
                PageRows<InterFlowAccount> pageRows = MiscServiceSngl.get().queryInterFlowAccountByPage(queryExpress, page);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    page = pageRows.getPage();
                    for (InterFlowAccount interFlowAccount : pageRows.getRows()) {
                        Label accountLabel = new Label(1, row, interFlowAccount.getAccount());
                        writableSheet.addCell(accountLabel);
                        Label nameLabel = new Label(2, row, interFlowAccount.getName());
                        writableSheet.addCell(nameLabel);
                        Label gameNameLabel = new Label(3, row, interFlowAccount.getGameName());
                        writableSheet.addCell(gameNameLabel);
                        Label userNumLabel = new Label(4, row, interFlowAccount.getUserNumber());
                        writableSheet.addCell(userNumLabel);
                        Label levelLabel = new Label(5, row, interFlowAccount.getLevel());
                        writableSheet.addCell(levelLabel);
                        Label dutyLabel = new Label(6, row, interFlowAccount.getDuty());
                        writableSheet.addCell(dutyLabel);
                        Label lordLabel = new Label(7, row, interFlowAccount.getLord());
                        writableSheet.addCell(lordLabel);
                        Label manuLabel = new Label(8, row, interFlowAccount.getManufacturer());
                        writableSheet.addCell(manuLabel);
                        Label gameCateLabel = new Label(9, row, interFlowAccount.getGameCategory());
                        writableSheet.addCell(gameCateLabel);
                        Label gameTypeLabel = new Label(10, row, interFlowAccount.getGameType());
                        writableSheet.addCell(gameTypeLabel);
                        Label platLabel = new Label(11, row, interFlowAccount.getPlatform());
                        writableSheet.addCell(platLabel);
                        Label themeLabel = new Label(12, row, interFlowAccount.getTheme());
                        writableSheet.addCell(themeLabel);

                        Label areaLabel = new Label(13, row, interFlowAccount.getPublishArea());
                        writableSheet.addCell(areaLabel);

                        Label lastDateLabel = new Label(14, row, interFlowAccount.getLastPostDate());
                        writableSheet.addCell(lastDateLabel);

                        if (interFlowAccount.getType().equals(InterFlowType.QQ)) {
                            Label typeLabel = new Label(15, row, "QQ群");
                            writableSheet.addCell(typeLabel);
                        } else if (interFlowAccount.getType().equals(InterFlowType.TIEBA)) {
                            Label typeLabel = new Label(15, row, "百度贴吧");
                            writableSheet.addCell(typeLabel);
                        }

                        row = row + 1;
                    }
                }

            } while (!page.isLastPage());

            workbook.write();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        return new ModelAndView("forward:/misc/qq/list", mapMessage);
    }

    public static void main(String[] args) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//            InputStream is = new FileInputStream("C:\\Users\\zhitaoshi\\Desktop\\贴吧Q群筛选属性需求333.xls");
            File file = new File("C:\\Users\\zhitaoshi\\Desktop\\贴吧Q群筛选属性需求333.xls");
            WorkbookSettings workbookSettings = new WorkbookSettings();

            workbookSettings.setEncoding("GB2312");
//            Workbook rwb = Workbook.getWorkbook(is, workbookSettings);
            Workbook rwb = Workbook.getWorkbook(file, workbookSettings);

            //Sheet st = rwb.getSheet("0")这里有两种方法获取sheet表,1为名字，而为下标，从0开始
            Sheet[] sheets = rwb.getSheets();
            if (sheets.length > 0) {
                for (Sheet st : sheets) {
                    int columns = st.getColumns();
                    int rows = st.getRows();
                    for (int k = 1; k < rows; k++) {//行
                        for (int i = 1; i < columns; i++) {//列

                            Cell c00 = st.getCell(i, k);
                            //通用的获取cell值的方式,返回字符串
                            String strc00 = c00.getContents();
                            //获得cell具体类型值的方式
                            if (c00.getType() == CellType.LABEL) {
                                LabelCell labelc00 = (LabelCell) c00;
                                strc00 = labelc00.getString();
                            }
                            //excel 类型为时间类型处理;
                            if (c00.getType() == CellType.DATE) {
                                DateCell dc = (DateCell) c00;
                                strc00 = sdf.format(dc.getDate());

                            }
                            //excel 类型为数值类型处理;
                            if (c00.getType() == CellType.NUMBER || c00.getType() == CellType.NUMBER_FORMULA) {
                                NumberCell nc = (NumberCell) c00;
                                strc00 = "" + nc.getValue();
                            }

                            //输出
                            System.out.println(">" + strc00);

                            //列，行
                            //           data2=String.valueOf(st.getCell(1,k).getContents());
                            //           data2=data2.replace("/", "-");
                            //           java.util.Date dt=sdf.parse(data2);
                            //           System.out.println(sdf.format(dt));
                            //
                        }
                    }
                }
            }


            //关闭
            rwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
