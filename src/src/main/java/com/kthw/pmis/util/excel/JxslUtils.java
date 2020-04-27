//package com.kthw.pmis.util.excel;
//import org.jxls.common.Context;
//import org.jxls.expression.JexlExpressionEvaluator;
//import org.jxls.transform.Transformer;
//import org.jxls.transform.poi.PoiTransformer;
//import org.jxls.util.JxlsHelper;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * jxls2.4 导出excle工具类
// * @SINCE 2018/10/9 17:06
// * @AUTHOR LCM
// * @Param
// * @return
// */
//public class JxslUtils{
//
//    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException{
//        Context context = PoiTransformer.createInitialContext();
//        if (model != null) {
//            for (String key : model.keySet()) {
//                context.putVar(key, model.get(key));
//            }
//        }
//        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
//        if(is==null){
//            return;
//        }
//        Transformer transformer  = jxlsHelper.createTransformer(is, os);
//        //获得配置
//        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
//        //设置静默模式，不报警告
////        evaluator.getJexlEngine().setSilent(true);
//        //函数强制，自定义功能
//        Map<String, Object> funcs = new HashMap<String, Object>();
//        funcs.put("utils", new JxslUtils());    //添加自定义功能
//        evaluator.getJexlEngine().setFunctions(funcs);
//        //必须要这个，否者表格函数统计会错乱
//        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
//    }
//
//    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
//        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
//    }
//
//    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
//        File template = getTemplate(templatePath);
//        if(template != null){
//            exportExcel(new FileInputStream(template), os, model);
//        } else {
//            throw new Exception("Excel 模板未找到。");
//        }
//    }
//
//    //获取jxls模版文件
//    public static File getTemplate(String path){
//        File template = new File(path);
//        if(template.exists()){
//            return template;
//        }
//        return null;
//    }
//}