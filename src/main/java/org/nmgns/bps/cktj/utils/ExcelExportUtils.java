package org.nmgns.bps.cktj.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ExcelExportUtils {

    public static void exportExcel(HttpServletResponse response,
                                   List<Map<String,Object>> data,
                                   String fileName,
                                   int columnLines) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter();

        writer.merge(columnLines-1,fileName);

        writer.write(data,true);

        String name = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        response.reset();
        response.setCharacterEncoding("gb2312");
        response.setContentType("application/OCTET-STREAM;charset=gb2312");
        response.setHeader("pragma", "no-cache");
        response.addHeader("Content-Disposition","attachment;filename=\"" + name +".xls\"");
        OutputStream out = response.getOutputStream();

        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

}
