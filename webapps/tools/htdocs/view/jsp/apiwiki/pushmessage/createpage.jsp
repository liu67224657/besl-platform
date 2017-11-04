<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加游戏标签</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript" src="/static/include/js/vue.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#form').submit(function () {

            });

//            $('input[name=sendtype]').click(function(){
//                var val=$(this).val();
//                 if(val==0){
//                  $('#tr_delayed_option').css("display","");
//                     $('#tr_delayed_option').hide();
//                 }
//            })
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >> 推送消息</td>
    </tr>
    <tr>

        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">推送消息</td>
                </tr>
                <tr>
                    <td>
                        <form action="/apiwiki/pushmessage/create" method="post" id="form">
                            <table border="0" cellspacing="0" cellpadding="0" width="80%">
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_header_td"></td>
                                </tr>
                                <c:if test="${fn:length(errorMsg)>0}">
                                    <tr>
                                        <td height="1" colspan="3" class="fontcolor_input_hint">
                                            <fmt:message key="${errorMsg}" bundle="${errorProps}"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td class="edit_table_value_td" colspan="3">
                                        <input type="radio" name="sendtype" value="delayed"
                                               v-on:click="displayDeplayed($event.currentTarget)" id="send_delayed"
                                               v-model="currentValue.sendtype">预约发送&nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="radio" name="sendtype" value="now"
                                               v-on:click="displayDeplayed($event.currentTarget)"
                                               v-model="currentValue.sendtype">立即发送
                                    </td>
                                </tr>
                                <tr id="tr_delayed_option" v-if="sendType=='delayed'">
                                    <td class="edit_table_value_td" colspan="3" width="" height="1">
                                        <table>
                                            <tr>
                                                <td width="50">
                                                    <select name="year" v-model="currentValue.year">
                                                        <option value="">年</option>
                                                        <option v-for="y in years">{{y}}</option>
                                                    </select>
                                                </td>
                                                <td width="50">
                                                    <select name="month" v-model="currentValue.month">
                                                        <option value="">月</option>
                                                        <option v-for="m in months">{{m}}</option>
                                                    </select>
                                                </td>
                                                <td width="50">
                                                    <select name="day" v-model="currentValue.day">
                                                        <option value="">日</option>
                                                        <option v-for="d in days">{{d}}</option>
                                                    </select>
                                                </td>
                                                <td width="50">
                                                    <select name="hour" v-model="currentValue.hour">
                                                        <option value="">小时</option>
                                                        <option v-for="h in hours">{{h}}</option>
                                                    </select>
                                                </td>
                                                <td width="">
                                                    <select name="min" v-model="currentValue.min">
                                                        <option value="">分钟</option>
                                                        <option v-for="min in mins">{{min}}</option>
                                                    </select>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr height="30">
                                    <td colspan="3" height="" class="">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="edit_table_value_td" colspan="3">
                                        <table>
                                            <tr>
                                                <td>筛选</td>
                                                <td>
                                                    <select name="profiletag" v-model="currentValue.profiletag">
                                                        <option value="">全部</option>
                                                        <option v-for="pt in profiletags" v-bind:value="pt.code">
                                                            {{pt.desc}}
                                                        </option>
                                                    </select>
                                                </td>
                                                <td class="fontcolor_dsp_hint">暂不支持推制定用户。以后再说！</td>
                                                <%--暂时不支持--%>
                                                <%--<td>--%>
                                                <%--<input type="" name="" placeholder="用户名/ID">--%>
                                                <%--</td>--%>
                                                <%--<td>--%>
                                                <%--<input type="button" name="" value="搜索">--%>
                                                <%--</td>--%>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr height="30">
                                    <td height="" class="" colspan="3">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="edit_table_value_td" colspan="3">
                                        <table>
                                            <tr>
                                                <td>平台</td>
                                                <td>
                                                    <select name="platformtag"
                                                            @change="checkChannelByPlatform($event.currentTarget.value)"
                                                            v-model="currentValue.platformtag">
                                                        <option value="">全部</option>

                                                        <option v-for="platform in platforms" :value="platform.code">
                                                            {{platform.name}}
                                                        </option>
                                                    </select>
                                                </td>
                                                <td>渠道</td>
                                                <td>
                                                    <select name="channeltag" v-model="currentValue.channeltag">
                                                        <option value="">全部</option>
                                                        <option v-for="channel in channels" :value="channel">
                                                            {{channel}}
                                                        </option>
                                                    </select>
                                                </td>
                                                <td>版本</td>
                                                <td>
                                                    <select name="verisontag" v-model="currentValue.verisontag">
                                                        <option value="">全部</option>
                                                        <option v-for="v in versions" :value="v">{{v}}</option>
                                                    </select>
                                                </td>
                                                <td>小版本号</td>
                                                <td>
                                                    <select name="minverisontag" v-model="currentValue.minverisontag">
                                                        <option value="">全部</option>
                                                        <option v-for="minv in minversions" :value="minv">{{minv}}</option>
                                                    </select>
                                                </td>
                                                <td style="color: green">
                                                    *如果想给2.0.1推送，请选择小版本号，如果给2.0版本推送小版本号选择全部
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr height="30">
                                    <td height="" class="" colspan="3">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="edit_table_value_td" colspan="3">
                                        <table>
                                            <tr>
                                                <td>跳转类型</td>
                                                <td>
                                                    <select name="jt" @change="diplayJITips()"
                                                            v-model="currentValue.jt">
                                                        <option v-for="jt in jts" v-bind:value="jt.jt">{{jt.name}}
                                                        </option>
                                                    </select>
                                                </td>
                                                <td class="fontcolor_dsp_hint">*必填项</td>
                                                <td width="30"></td>
                                                <td>跳转信息</td>
                                                <td>
                                                    <input type="text" name="ji" size="60" :placeholder="jitips"
                                                           v-model="currentValue.ji"/>
                                                </td>
                                                <td></td>
                                            </tr>

                                        </table>
                                    </td>
                                </tr>
                                <tr height="30">
                                    <td height="" class="" colspan="3">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="edit_table_value_td" colspan="3">
                                        <table>
                                            <tr>
                                                <td>发送内容</td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">
                                                    <textarea name="body" id="" cols="100" rows="10"
                                                              v-model="currentValue.body"></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="edit_table_value_td" align="center">
                                        <input type="submit" value="推送">
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
<script>
    var app = new Vue({
        el: '#form',
        data: {
            years: [],
            months: [],
            days: [],
            hours: [],
            mins:[],
            sendType: false,
            platforms: [{code: 'IOS', name: 'IOS'}, {code: 'ANDROID', name: '安卓'}],
            channels: [],
            versions: ['1.2', '2.0'],
            minversions:['0','1','2','3','4','5','6','7','8','9'],
            profiletags: [
                {code: 'VERIFY', desc: '<fmt:message key="verifyprofile.type.VERIFY"  bundle="${toolsProps}"/>'},
                {code: 'UNVERIFY', desc: '<fmt:message key="verifyprofile.type.UNVERIFY"  bundle="${toolsProps}"/>'}
            ],
            jts: [
                {jt: -1, name: '打开应用', tips: '不用填写'},
                {jt: -2, name: '跳转到webview页面', tips: '填写完整的URL'},
                {jt: -3, name: '跳转到(我的)系统通知', tips: '不用填写'},
                {jt: 200, name: '跳转点评详情页面', tips: '填写，游戏ID'},
                {jt: 201, name: '跳转消息详情页面', tips: '填写，需要显示的文本内容'}
            ],

            currentValue: {
                year: '${year}',
                month: '${month}',
                day: '${day}',
                hour: '${hour}',
                min:'${min}',
                jt:${jt==null?-1:jt},
                profiletag: '${profiletag}',
                channeltag: '${channeltag}',
                verisontag: '${verisontag}',
                minverisontag: '${minverisontag}',
                ji: '${ji}',
                body: '${body}',
                sendtype: '${sendtype!=null?sendtype:now}',
                platformtag: '${platformtag}',
            },
            jitips: '不用填写'
        },
        mounted: function () {
            this.sendType = this.currentValue.sendtype;
            var platform = this.currentValue.platformtag;
            this.checkChannelByPlatform(platform);
            this.initData();
        },
        methods: {
            initData: function () {
                <c:forEach var="year" items="${years}" >
                this.years.push('${year}');
                </c:forEach>
                <c:forEach var="month" items="${months}" >
                this.months.push('${month}');
                </c:forEach>
                <c:forEach var="day" items="${days}" >
                this.days.push('${day}');
                </c:forEach>
                <c:forEach var="hour" items="${hours}" >
                this.hours.push('${hour}');
                </c:forEach>
                <c:forEach var="min" items="${mins}" >
                this.mins.push('${min}');
                </c:forEach>



            },
            displayDeplayed: function (target) {
                this.sendType = target.value;
            },
            checkChannelByPlatform: function (value) {
                if(value==''){
                    return;
                }

                var platform =value;
                if (platform == 'IOS') {
                    this.channels = ['appstore']
                } else if (platform == 'ANDROID') {
                    this.channels = ['joyme','c360','tencent','hiapk','anzhi','wandoujia','baidu','gfan','appchina','mumayi','3g','xiaomi','c91','uc','bsbdj','legc','pp','yingyongbao','sogou','jinli','meizu','nduo','paojiao','lenovo','ali','liqu','oppo','vivo']
                }
            },
            diplayJITips: function () {
                for (var i = 0; i < this.jts.length; i++) {
                    var jt = this.jts[i];
                    if (jt.jt == this.currentValue.jt) {
                        this.jitips = jt.tips;
                        break;
                    }
                }
            }

        }
    })
</script>
</html>