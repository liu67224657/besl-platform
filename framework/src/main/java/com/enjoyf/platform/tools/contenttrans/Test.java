/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.contenttrans;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-3 下午4:48
 * Description:
 */
public class Test {

    public static void main(String[] args) {
        String txt = "{\"photo\":[{\"des\":\"菊花盛开\",\"s\":\"/r001/image/2011/07/49/453859528B76397A5A12A9D2F36BA702_S.jpg\",\"b\":\"/r001/image/2011/07/49/453859528B76397A5A12A9D2F36BA702.jpg\",\"m\":\"/r001/image/2011/07/49/453859528B76397A5A12A9D2F36BA702_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/28/87D07F90F10C40ED8075F45C042FE6F6_S.jpg\",\"b\":\"/r001/image/2011/07/28/87D07F90F10C40ED8075F45C042FE6F6.jpg\",\"m\":\"/r001/image/2011/07/28/87D07F90F10C40ED8075F45C042FE6F6_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/91/A49FE8914DF0EADA4D4B7D530D7FA5BA_S.jpg\",\"b\":\"/r001/image/2011/07/91/A49FE8914DF0EADA4D4B7D530D7FA5BA.jpg\",\"m\":\"/r001/image/2011/07/91/A49FE8914DF0EADA4D4B7D530D7FA5BA_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/39/BD6661231506E0FE52C15A3A77ECC1E7_S.jpg\",\"b\":\"/r001/image/2011/07/39/BD6661231506E0FE52C15A3A77ECC1E7.jpg\",\"m\":\"/r001/image/2011/07/39/BD6661231506E0FE52C15A3A77ECC1E7_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/25/06D3BC740E8BA0C8F64427649F537269_S.jpg\",\"b\":\"/r001/image/2011/07/25/06D3BC740E8BA0C8F64427649F537269.jpg\",\"m\":\"/r001/image/2011/07/25/06D3BC740E8BA0C8F64427649F537269_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/85/4F6A306C94AF679657CED7273B5AD4EA_S.jpg\",\"b\":\"/r001/image/2011/07/85/4F6A306C94AF679657CED7273B5AD4EA.jpg\",\"m\":\"/r001/image/2011/07/85/4F6A306C94AF679657CED7273B5AD4EA_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/98/571E32A9497BC9CE85A05DDA04543FD6_S.jpg\",\"b\":\"/r001/image/2011/07/98/571E32A9497BC9CE85A05DDA04543FD6.jpg\",\"m\":\"/r001/image/2011/07/98/571E32A9497BC9CE85A05DDA04543FD6_M.jpg\"},{\"des\":\"\",\"s\":\"/r001/image/2011/07/0/538DCF9D8B98AC9A193A01F25D975AD2_S.jpg\",\"b\":\"/r001/image/2011/07/0/538DCF9D8B98AC9A193A01F25D975AD2.jpg\",\"m\":\"/r001/image/2011/07/0/538DCF9D8B98AC9A193A01F25D975AD2_M.jpg\"}]}";

        OldImageContentSet set = OldImageContentSet.parse(txt);

        System.out.println(set);
    }
}
