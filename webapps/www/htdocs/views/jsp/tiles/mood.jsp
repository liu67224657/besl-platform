<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="rel_face reldiv" id="${moodId}">
    <table cellspacing="0" cellpadding="0" border="0" class="div_warp">
        <tbody>
        <tr>
            <td class="top_l"></td>
            <td class="top_c"></td>
            <td class="top_r"></td>
        </tr>
        <tr>
            <td class="mid_l"></td>
            <td class="mid_c">
                <div class="p_face">
                    <div class="cont_rel_t">
                        <span>潘斯特表情</span>
                        <a id="close_chat_mood" class="relCancel" id="faceHide" href="javascript:void(0)"></a>
                    </div>
                    <div class="p_face_cont clearfix">
                        <div class="face_con clearfix">
                            <c:forEach items="${moodMap}" var="mood">
                                <a href="javascript:void(0)" title="<c:out value="${mood.value.code}"/>"><img
                                    class="<c:out value="${mood.value.imgClass}"/>" src="<c:out
                                    value="${mood.value.imgUrl}"/>"/></a>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="p_summuray clearfix">潘斯特是个充满弹性的小外星人，耳朵其实是触手。</div>
                </div>
            </td>
            <td class="mid_r"></td>
        </tr>
        <tr>
            <td class="bottom_l"></td>
            <td class="bottom_c"></td>
            <td class="bottom_r"></td>
        </tr>
        </tbody>
    </table>
</div>