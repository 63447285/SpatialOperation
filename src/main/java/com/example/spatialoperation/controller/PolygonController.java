package com.example.spatialoperation.controller;

import com.example.spatialoperation.conf.DataSourceUtil;
import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.myCallable.ClipCallable;
import com.example.spatialoperation.myCallable.IntersectCallable;
import com.example.spatialoperation.myCallable.SelectDataCallable;
import com.example.spatialoperation.myCallable.UnionCallable;
import com.example.spatialoperation.service.PointService;
import com.example.spatialoperation.service.PolygonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class PolygonController {
    @Autowired
    private PolygonService polygonService;
    //线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(8);

    private static Logger log = LoggerFactory.getLogger(PolygonController.class);

    //id获取MyPolygon
    @RequestMapping("/polygon/getById")
    public MyPolygon getByID(int id) {
        return polygonService.getPolygonByID(id);
    }

    @RequestMapping("polygon/getMyPolygonById")
    public MyPolygon getMyPolygonById(String tempid) {
        DataSourceUtil.setDB("db2");
        int id=0;
        id=Integer.parseInt(tempid);
        return polygonService.getMyPolygonById(id);
    }
    ;
    @RequestMapping("polygon/getMyPolygonByIdNew")
    public MyPolygon getMyPolygonByIdNew(String tempid) {
        DataSourceUtil.setDB("db2");
        int id=0;
        id=Integer.parseInt(tempid);
        return polygonService.getMyPolygonByIdNew(id);
    }

    //多线程分页获取所有多边形shape
    @RequestMapping("/polygon/getAllPolygonGeometry")
    public List<String> getAllPolygonGeometry() throws ExecutionException, InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<List<String>>> futures = new ArrayList<>();
        int index = 0;
        int num = 8000;
        SelectDataCallable c = null;
        // 2个线程
        for (int subId = 1; subId <= 2; subId++) {
            log.info("执行次数" + String.valueOf(subId));
            c = new SelectDataCallable(polygonService, index, num);
            // 提交线程任务
            Future<List<String>> f = executorService.submit(c);
            futures.add(f);
            index = index + num;
        }
        log.info("futures数量：" + String.valueOf(futures.size()));
        List<String> result = new ArrayList<>();
        for (Future<List<String>> f : futures) {
            try {
                result.addAll(f.get());
            } catch (Exception e) {
            }
        }
        watch.stop();
        log.info("result数量：" + String.valueOf(result.size()));
        log.info("获取所有多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return result;
    }

    ;


//    旧方法
//    @RequestMapping("/polygon/getIntersectPolygons")
//    public List<MyPolygon> getIntersectPolygons(String wkt) throws ParseException {
//        return polygonService.getIntersectPolygons(wkt);
//    };
//
//    @RequestMapping("/polygon/getIntersectionClippingGeometrys")
//    public List<String> getIntersectionClippingGeometrys(String wkt) throws ParseException {
//        return polygonService.getIntersectionClippingGeometry(wkt);
//    }
//
//    @RequestMapping("/polygon/getUnionGeometry")
//    public String getUnionGeometry(String dlmc) throws ParseException {
//        return polygonService.getUnionGeometry(dlmc);
//    }

    //新方法 使用postgis空间索引
    //多边形相交
    @RequestMapping("/polygon/getPolygonByIntersects")
    public List<MyPolygon> getPolygonByIntersects(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<List<MyPolygon>>> futures = new ArrayList<>();
        int index = 0;
        int num = 400000;
        IntersectCallable c = null;
        // 8个线程
        for (int subId = 1; subId <= 8; subId++) {
            c = new IntersectCallable(polygonService, wkt, index, num);
            // 提交线程任务
            Future<List<MyPolygon>> f = executorService.submit(c);
            futures.add(f);
            index = index + num;
        }
        log.info("相交计算中......");
        List<MyPolygon> result = new ArrayList<>();
        for (Future<List<MyPolygon>> f : futures) {
            try {
                result.addAll(f.get());
            } catch (Exception e) {
            }
        }
        watch.stop();
        log.info("result数量：" + String.valueOf(result.size()));
        log.info("获取所有相交多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        polygonService.deletePolygon1();
        DataSourceUtil.setDB("db2");
        for(int i=0;i<result.size();i++){
            polygonService.insert1(result.get(i));
        }
        if(result.size()>9999){
            for(int i=0;i< result.size();i++){
                Geometry g=new WKTReader().read(result.get(i).getShape());
                Point point=g.getInteriorPoint();
                result.get(i).setShape(new WKTWriter().write(point));
            }
        }
        return result;
    }

    ;

    @RequestMapping("/polygon/getPolygonByClipping")
    public List<MyPolygon> getPolygonByIntersectionClipping(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<MyPolygon> result=polygonService.getPolygonByClipping(wkt,0,400000);
//        List<Future<List<MyPolygon>>> futures = new ArrayList<>();
//        int index = 0;
//        int num = 400000;
//        ClipCallable c = null;
//        // 8个线程
//        for (int subId = 1; subId <= 8; subId++) {
//            c = new ClipCallable(polygonService, wkt, index, num);
//            // 提交线程任务
//            Future<List<MyPolygon>> f = executorService.submit(c);
//            futures.add(f);
//            index = index + num;
//        }
        log.info("裁剪中......");
//        List<MyPolygon> result = new ArrayList<>();
//        for (Future<List<MyPolygon>> f : futures) {
//            try {
//                result.addAll(f.get());
//            } catch (Exception e) {
//            }
//        }
        watch.stop();
        log.info("result数量：" + String.valueOf(result.size()));
        log.info("裁剪多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        DataSourceUtil.setDB("db2");
        polygonService.deletePolygon2();
        for(int i=0;i<result.size();i++){
            polygonService.insert2(result.get(i));
        }
        if(result.size()>9999){
            for(int i=0;i< result.size();i++){
                Geometry g=new WKTReader().read(result.get(i).getShape());
                Point point=g.getInteriorPoint();
                result.get(i).setShape(new WKTWriter().write(point));
            }
        }
        return result;
    }

    ;

    @RequestMapping("/polygon/getPolygonByUnion")
    public String getPolygonByUnion(String dlmc) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<String>> futures = new ArrayList<>();
        int index = 0;
        int num = 400000;
        UnionCallable c = null;
        // 8个线程
        for (int subId = 1; subId <= 8; subId++) {
            c = new UnionCallable(polygonService, index, num, dlmc);
            // 提交线程任务
            Future<String> f = executorService.submit(c);
            futures.add(f);
            index = index + num;
        }
        log.info("合并中......");
        List<String> result = new ArrayList<>();
        for (Future<String> f : futures) {
            try {
                result.add(f.get());
            } catch (Exception e) {
            }
        }
        Geometry geometry = new WKTReader().read(result.get(0));
        for (int i = 1; i < result.size(); i++) {
            if (null != result.get(i)) {
                geometry = geometry.union(new WKTReader().read(result.get(i)));
            }
        }
        WKTWriter writer = new WKTWriter(2);
        watch.stop();
        log.info("合并多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return writer.write(geometry);
    }

    ;

    @RequestMapping("/polygon/insertPolygon")
    public MyPolygon insertPolygon() throws ParseException {
        DataSourceUtil.setDB("db2");
        MyPolygon myPolygon = new MyPolygon(1, "POLYGON ((112.93050815200006 22.808261919666762, 112.93054090300011 22.80822370366671, 112.930550377 22.808138697666692, 112.93058321600004 22.808045218666773, 112.93065813800003 22.80791778866671, 112.93073765600003 22.807837124666698, 112.93080313100003 22.807777697666754, 112.93091060900005 22.8077353276668, 112.93117690000005 22.807671912666795, 112.93147119300006 22.80762128566678, 112.93153194900003 22.807591608666723, 112.93159273300009 22.80754492666677, 112.93162550300005 22.807493957666725, 112.931658294 22.807430236666782, 112.93170508200006 22.807375034666745, 112.93176585200011 22.807336855666783, 112.93180795500004 22.807290149666738, 112.93183138500002 22.807239168666783, 112.93186417000008 22.807179697666747, 112.93186906000004 22.807039421666776, 112.93186452400002 22.806954397666757, 112.93185527800006 22.806894871666717, 112.93182735700005 22.806831070666767, 112.93178541500005 22.80677575366672, 112.93171545200003 22.80672040066676, 112.93163614900004 22.806665033666707, 112.93157086300006 22.80660543566675, 112.93153359700008 22.806545873666707, 112.93144507500004 22.806413977666807, 112.93130994200006 22.80622675866677, 112.93117933700007 22.80612881566675, 112.93105338200004 22.806043631666718, 112.93092278500008 22.8059414366667, 112.93084823300003 22.80583506566669, 112.9308156710001 22.805754254666756, 112.93081577900001 22.805686239666723, 112.93084874400009 22.805511992666695, 112.93087712600003 22.80528247766671, 112.93088667500011 22.805150710666734, 112.93087745600006 22.805074180666708, 112.93084021800009 22.804997614666767, 112.93074705500001 22.804848708666704, 112.930649208 22.80470829866681, 112.93060730700009 22.804627475666784, 112.9305934050001 22.80455944166675, 112.93062059600004 22.804517362666733, 112.9306106240001 22.804498533666774, 112.93058742300005 22.80440498266676, 112.9305268560001 22.80431563166674, 112.93039627400003 22.80420493566673, 112.93020740000013 22.804070345666783, 112.93003624800009 22.804116648666795, 112.92983336700009 22.804168553666738, 112.92967018800009 22.804204456666778, 112.9294717480001 22.804236300666748, 112.92929975900009 22.80426817866672, 112.92920270400005 22.804308181666745, 112.92914089300008 22.804364285666768, 112.92913091300005 22.80438780566676, 112.92911876600003 22.80441642766673, 112.92911426200001 22.804476618666737, 112.92908755400002 22.804637110666782, 112.92906511300004 22.80488589766676, 112.92903403500013 22.805022304666767, 112.92896750700004 22.80527103366674, 112.92889674400004 22.80541140066672, 112.92875979200008 22.80558779866675, 112.92852116900005 22.80593261566677, 112.9283444850001 22.806141065666786, 112.92814144500008 22.806289283666704, 112.92793405500007 22.80640137766674, 112.92767817900003 22.80650537866673, 112.92741349400006 22.806605355666743, 112.92721938600008 22.806681346666725, 112.92696346500004 22.80681343966673, 112.92680458800004 22.806913556666753, 112.92664563900004 22.807057818666788, 112.92651153700004 22.807170165666758, 112.92667782600003 22.807342852666746, 112.92687938800002 22.807625526666776, 112.92702331600002 22.807855581666715, 112.92711678200003 22.808059300666784, 112.92721018500004 22.80830242466674, 112.92724755900011 22.808466410666718, 112.92725441300004 22.80846703266677, 112.9274428360001 22.808500391666687, 112.92758495300004 22.808542719666725, 112.92764442800008 22.808569888666753, 112.92770387900002 22.808612105666782, 112.92774682200012 22.808639251666808, 112.92781293500008 22.808651378666745, 112.927859218 22.808657460666716, 112.92796172800004 22.808654587666744, 112.92812711700005 22.808618688666794, 112.9283156320001 22.808594859666776, 112.92844789700003 22.808595034666702, 112.92865952000004 22.808595315666814, 112.928930639 22.808610723666757, 112.92907282800005 22.8086079026667, 112.92917203600008 22.808602013666746, 112.92928120200006 22.808572059666755, 112.92938712000003 22.808505981666706, 112.92954595700012 22.808430944666714, 112.92963527800005 22.808403973666703, 112.92966504300001 22.808401002666812, 112.92969147200007 22.80841608666673, 112.92970131400008 22.808464257666717, 112.92968116800002 22.808656863666755, 112.9296509540001 22.808942761666785, 112.92965082000009 22.80902703866674, 112.9296424980001 22.809182842666754, 112.92967077300005 22.809166273666715, 112.92981574100006 22.809043186666745, 112.92991867800005 22.80892004366677, 112.9300030040001 22.80875436766674, 112.93001729700003 22.808575845666713, 112.9300127490001 22.808499322666776, 112.93002219000009 22.80843557066674, 112.93006426100001 22.808410119666767, 112.93012030100003 22.808410193666774, 112.930209033 22.808410310666694, 112.93030244700009 22.80840193166671, 112.93039123900006 22.808363789666753, 112.93045669300007 22.8083171146668, 112.93050815200006 22.808261919666762))", "测试");
        polygonService.insert1(myPolygon);
        return myPolygon;
    }

}
