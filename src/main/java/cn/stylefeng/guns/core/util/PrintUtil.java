package cn.stylefeng.guns.core.util;

import cn.stylefeng.guns.core.tools.Excel2Pdf;
import cn.stylefeng.guns.core.tools.ExcelObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 打印相关的方法，excel的操作呀，二维码的生成啊
 * @author wuzh
 * @date 2018/11/21
 * @description
 */
public class PrintUtil {
    /**
     * 生成二维码图片
     * @param pathFix 图片保存路径
     * @param saveName 保存的名称
     * @param content 二维码内容
     * @return 二维码图片路径
     */
    public static String crcateQRCode(String pathFix,String saveName,String content){
        int width = 300;
        int height = 300;
        String format = "png";
        String _contcent = content;
        //定义二维码参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN,2);
        String saveUrl = pathFix + saveName +".png";

        //生成二维码
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(_contcent, BarcodeFormat.QR_CODE, width, height, hints);
            Path file = new File(saveUrl).toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, format, file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saveUrl;
    }

    /**
     * 插入图片到excel
     * @param imgUrl 图片路径
     * @param wb excel表对象
     * @param col 起始列
     * @param row 起始行
     */
    public static void insertImgToExcel(String imgUrl,Workbook wb,int col,int row){

        try {
            InputStream is = new FileInputStream(imgUrl);
            //读取图片文件得到字节
            byte[] bytes = IOUtils.toByteArray(is);
            //向Excel添加一张图片,并返回该图片在Excel中的图片集合中的下标

            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = wb.getCreationHelper();
            //创建一个绘图对象
            Drawing drawing = wb.getSheetAt(0).createDrawingPatriarch();
            //创建锚点
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(col);//从0开始
            anchor.setRow1(row);//从0开始

            //根据锚点和图片下标创建并绘制一张图片
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            //相对于图片的左上角自动适应大小
            //===========>>>>>>>>>[注意]<<<<<<=================
            //picture.resize() 仅仅只是针对这两种类型的图片 JPEG 和 PNG.
            //其他格式暂时不支持
            pict.resize();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 保存excel文件
     * @param workbook 表格对象
     * @param savePathFix 保存的目录
     * @param saveName 保存的名称
     * @return 返回 excel路径
     * @throws IOException 文件处理异常
     */
    public  static  String saveToExcel(Workbook workbook,String savePathFix,String saveName) throws IOException{
        //创建路径
        File savefile = new File(savePathFix);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }

        //保存文件
        String saveUrl = savePathFix + saveName + ".xls";
        FileOutputStream fos = new FileOutputStream(saveUrl);
        workbook.write(fos);
        fos.close();
        return saveUrl;
    }

    /**
     *
     * @param fileFix 保存文件夹位置
     * @param excelFilePath  excel文件位置
     * @param pdfSavePath pdf 保存位置
     * @param pdfSaveName pdf 保存名称
     * @return pdf 保存最终位置
     * @throws Exception 错误
     */
    public static String saveToPdf(String fileFix,String excelFilePath,String pdfSavePath,String pdfSaveName) throws Exception{
        FileInputStream file = new FileInputStream(excelFilePath);

        //创建路径
        File savefile = new File(pdfSavePath);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }

        //输出文件
        FileOutputStream outFile = new FileOutputStream(pdfSavePath + pdfSaveName  + ".pdf");
        //转换excel为pdf
        List<ExcelObject> objects = new ArrayList<ExcelObject>();
        objects.add(new ExcelObject(null,file));
        Excel2Pdf pdf = new Excel2Pdf(objects , outFile);
        //转换
        pdf.convert();
        outFile.close();

        return fileFix + pdfSaveName  + ".pdf";
    }
}
