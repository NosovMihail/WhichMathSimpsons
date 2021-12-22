import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Fourth extends Application {

    private int selectedFunk = 0;
    private int selectedMethod = 0;
    private int i = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Лаба 4");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        //ObservableList<String> funcs = FXCollections.observableArrayList("2x^3 - 5*x^2 - 3x + 21", "sin(x)/x", "5/x");
        //ComboBox<String> funcComboBox = new ComboBox<String>(funcs);
        //funcComboBox.setValue("2x^3 - 5*x^2 - 3x + 21");

        ObservableList<String> method = FXCollections.observableArrayList("Авто", "Линейная", "Квадратичная", "Степенная", "Экспоненциальная", "Логарифмическая");
        ComboBox<String> methodComboBox = new ComboBox<String>(method);
        methodComboBox.setValue("Авто");

        TextField leftBorder = new TextField("-5");
        TextField rightBorder = new TextField("5");
        FlowPane drawBorders = new FlowPane(new Label("Область отрисовки "), leftBorder, rightBorder);

        TextArea dots = new TextArea("x1 y1\nx2 y2\nx3...");
        FlowPane borders = new FlowPane(new Label("Точки "), dots);

        FileChooser fileChooser = new FileChooser();

        Button fileCh = new Button("Файл");

        fileCh.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    dots.clear();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String ln = br.readLine();
                        while(ln != null){
                            dots.appendText(ln + "\n");
                            ln = br.readLine();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //List<File> files = Arrays.asList(file);
                }
            }
        });

        Button selectBtn = new Button("Вычислить");

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x,y);
        numberScatterChart.setAnimated(false);

        Label ans = new Label("Ответ");
        Label ans2 = new Label();
        GridPane pane = new GridPane();
        //GridPane pane = new GridPane(numberScatterChart, funk1, funk2, borders, text, selectBtn, ans);

        ColumnConstraints column1 = new ColumnConstraints(300,300,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(200,200,Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(400,400,Double.MAX_VALUE);
        column3.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().add(column1);
        pane.getColumnConstraints().add(column2);
        pane.getColumnConstraints().add(column3);
        //pane.setGridLinesVisible(true);

        pane.add(numberScatterChart, 0, 0);
        GridPane.setColumnSpan(numberScatterChart, 3);
        pane.add(fileCh, 0,1);
        pane.add(methodComboBox, 0,2);
        //pane.add(funk2, 0,2);
        pane.add(borders, 1, 1);
        GridPane.setColumnSpan(borders, 2);
        pane.add(drawBorders, 1, 2);
        GridPane.setColumnSpan(drawBorders, 2);
        pane.add(selectBtn, 0,3);
        pane.add(ans, 1,3);
        pane.add(ans2, 1,4);
        GridPane.setColumnSpan(ans, 2);
        GridPane.setColumnSpan(ans2, 2);
        numberScatterChart.setTitle("График");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        series2.setName("Проверяемые точки");
        series1.setName("Функция");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        selectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                datas.clear();
                datas2.clear();
                switch (methodComboBox.getValue()){
                    case "Авто":
                        selectedMethod = 0;
                        break;
                    case "Линейная":
                        selectedMethod = 1;
                        break;
                    case "Квадратичная":
                        selectedMethod = 2;
                        break;
                    case "Степенная":
                        selectedMethod = 3;
                        break;
                    case "Экспоненциальная":
                        selectedMethod = 4;
                        break;
                    case "Логарифмическая":
                        selectedMethod = 5;
                        break;
                }
                String[] tempLines = dots.getText().split("\n");
                double[][] dotValues = new double[2][tempLines.length];
                for(int i = 0; i < tempLines.length; i++){
                    String[] oneLine = tempLines[i].split(" ");
                    dotValues[0][i] = Double.parseDouble(oneLine[0]);
                    dotValues[1][i] = Double.parseDouble(oneLine[1]);
                }

                double n = -5;
                double m = 5;
                try{
                    n = Double.parseDouble(leftBorder.getText());
                    m = Double.parseDouble(rightBorder.getText());
                    if(m < n){
                        double temp1 = m;
                        m = n;
                        n = temp1;
                    }
                }catch (Exception e){}
                Ansver p;
                switch (selectedMethod){
                    case 0:
                        p = auto(dotValues);
                        break;
                    case 1:
                        p = linear(dotValues);
                        break;
                    case 2:
                        p = recting(dotValues);
                        break;
                    case 3:
                        p = staging(dotValues);
                        break;
                    case 4:
                        p = exponing(dotValues);
                        break;
                    case 5:
                        p = loging(dotValues);
                        break;
                    default:
                        p = new Ansver(Type.LINEAR, 1, 0);
                        break;
                }
                switch (p.type){
                    case LINEAR:
                        ans.setText("Линейная апроксимация: " + p.a + "*x + " + p.b + "  r = " + p.c);
                        break;
                    case RECT:
                        ans.setText("Квадратичная апроксимация: " + p.a + "*x^2 + " + p.b + "*x + " + p.c);
                        break;
                    case STAGE:
                        ans.setText("Степенная апроксимация: " + p.a + "*x^" + p.b);
                        break;
                    case EXP:
                        ans.setText("Экспоненциальная апроксимация: " + p.a + "*e^" + p.b + "*x");
                        break;
                    case LOG:
                        ans.setText("Логарифмическая апроксимация: " + p.a + "*e^" + p.b + "*x");
                        break;
                }
                ans2.setText("d = " + calculateD(p, dotValues));
                build(datas, p, n, m);
                for(int i = 0; i < dotValues[0].length; i += 1){
                    if((dotValues[0][i] > n)&&(dotValues[0][i] < m)) {
                        datas2.add(new XYChart.Data(dotValues[0][i], dotValues[1][i]));
                    }
                    //datas2.add(new XYChart.Data(i,Math.cos(i)));
                }
                series1.setData(datas);
                series2.setData(datas2);
            }
        });

        datas.add(new XYChart.Data(0,0));
        datas2.add(new XYChart.Data(0,0));
        series1.setData(datas);
        series2.setData(datas2);
        Scene scene = new Scene(pane, 1200,800);
        scene.getStylesheets().add("Succ.css");
        numberScatterChart.getData().add(series1);
        numberScatterChart.getData().add(series2);
        primaryStage.setScene(scene);

        primaryStage.show();


    }

    private Ansver loging(double[][] dotValues) {
        double[][] newDots = new double[2][dotValues[0].length];
        for(int i = 0; i < dotValues[0].length; i++){
            if(dotValues[1][i] != 0) {
                newDots[1][i] = dotValues[1][i];
            }else {
                newDots[1][i] = 0;
            }
            if(dotValues[0][i] != 0) {
                newDots[0][i] = Math.log(dotValues[0][i]);
            }else{
                newDots[0][i] = 0;
            }
        }
        Ansver ans = linear(newDots);
        return new Ansver(Type.LOG, ans.a, ans.b);
    }

    private Ansver exponing(double[][] dotValues) {
        double[][] newDots = new double[2][dotValues[0].length];
        for(int i = 0; i < dotValues[0].length; i++){
            if(dotValues[0][i] != 0) {
                newDots[0][i] = dotValues[0][i];
            }else {
                newDots[0][i] = 0;
            }
            if(dotValues[1][i] != 0) {
                newDots[1][i] = Math.log(dotValues[1][i]);
            }else{
                newDots[1][i] = 0;
            }
        }
        Ansver ans = linear(newDots);
        return new Ansver(Type.EXP, Math.exp(ans.b), ans.a);
    }

    private Ansver staging(double[][] dotValues) {
        double[][] newDots = new double[2][dotValues[0].length];
        for(int i = 0; i < dotValues[0].length; i++){
            if(dotValues[0][i] != 0) {
                newDots[0][i] = Math.log(dotValues[0][i]);
            }else {
                newDots[0][i] = 0;
            }
            if(dotValues[1][i] != 0) {
                newDots[1][i] = Math.log(dotValues[1][i]);
            }else{
                newDots[1][i] = 0;
            }
        }
        Ansver ans = linear(newDots);
        return new Ansver(Type.STAGE, Math.exp(ans.b), ans.a);
    }

    public void build(ObservableList<XYChart.Data> datas2, Ansver p, double leftBorder, double rightBorder){
        if((p.type == Type.STAGE)||(p.type == Type.LOG) && leftBorder <= 0){
            leftBorder = 0.000001;
        }
        for(double i = leftBorder; i < rightBorder; i += (rightBorder - leftBorder)/1000){
            //System.out.println(p.a + " " + p.b + " " + i);
            datas2.add(new XYChart.Data(i, function(i, p)));
        }
    }

    private Ansver auto(double[][] dotValues) {
        Ansver[] best = new Ansver[5];
        best[0] = linear(dotValues);
        best[1] = recting(dotValues);
        best[2] = staging(dotValues);
        best[3] = exponing(dotValues);
        best[4] = loging(dotValues);
        Ansver minA = best[0];
        double minD = calculateD(best[0], dotValues);
        System.out.println(minD);
        for(int i = 1; i < best.length; i++){
            double D = calculateD(best[i], dotValues);
            System.out.println(D);
            if(D < minD){
                minD = D;
                minA = best[i];
            }
        }
        return minA;
    }

    private double calculateD(Ansver ansver, double[][] dotValues) {
        double sum = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            sum += Math.pow(function(dotValues[0][i], ansver) - dotValues[1][i], 2);
        }
        System.out.println(sum);
        return Math.sqrt(sum/dotValues[0].length);
    }

    private Ansver linear(double[][] dotValues) {
        double SX = 0;
        double SXX = 0;
        double SXY = 0;
        double SY = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            SX += dotValues[0][i];
            SXX += Math.pow(dotValues[0][i], 2);
            SXY += dotValues[0][i]*dotValues[1][i];
            SY += dotValues[1][i];
        }
        //System.out.println(SX + "\t" + SY + "\t" + SXX + "\t" + SXY);
        double a = (SXY*dotValues[0].length - SX*SY)/(SXX*dotValues[0].length - SX*SX);
        double b = (SXX*SY - SX*SXY)/(SXX*dotValues[0].length - SX*SX);
        double sumX = 0;
        double sumY = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            sumX += dotValues[0][i];
            sumY += dotValues[1][i];
        }
        sumX /= dotValues[0].length;
        sumY /= dotValues[0].length;
        double chisl = 0;
        double znamX = 0;
        double znamY = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            chisl += (dotValues[0][i] - sumX)*(dotValues[1][i] - sumY);
            znamX += Math.pow(dotValues[0][i] - sumX, 2);
            znamY += Math.pow(dotValues[1][i] - sumY, 2);
        }
        double c = chisl/Math.sqrt(znamX*znamY);
        return new Ansver(Type.LINEAR, a, b, c);
    }

    private Ansver recting(double[][] dotValues) {
        double[][] coef = new double[3][3];
        double[] coefB = new double[3];
        double SX = 0;
        double SXX = 0;
        double SXXX = 0;
        double SXXY = 0;
        double SXXXX = 0;
        double SXY = 0;
        double SY = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            SX += dotValues[0][i];
            SXX += Math.pow(dotValues[0][i], 2);
            SXXY += Math.pow(dotValues[0][i], 2)*dotValues[1][i];
            SXXX += Math.pow(dotValues[0][i], 3);
            SXXXX += Math.pow(dotValues[0][i], 4);
            SXY += dotValues[0][i]*dotValues[1][i];
            SY += dotValues[1][i];
        }
        //System.out.println(SX + "\t" + SY + "\t" + SXX + "\t" + SXY + "\t" + SXXY + "\t" + SXXX + "\t" + SXXXX);
        coef[0][0] = dotValues[0].length;
        coef[0][1] = SX;
        coef[0][2] = SXX;
        coefB[0] = SY;
        coef[1][0] = SX;
        coef[1][1] = SXX;
        coef[1][2] = SXXX;
        coefB[1] = SXY;
        coef[2][0] = SXX;
        coef[2][1] = SXXX;
        coef[2][2] = SXXXX;
        coefB[2] = SXXY;
        return Gauss.calculate(Type.RECT, 3, coef, coefB);
    }

    private double function(double x, Ansver p) {
        if(Math.round(x * Math.pow(10, 9))/Math.pow(10, 9) == Math.round(x)){
            x = Math.round(x);
        }
        double h = 0;
        switch (p.type){
            case LINEAR:
                h = line(x, p);
                break;
            case RECT:
                h = rect(x, p);
                break;
            case STAGE:
                h = stage(x, p);
                break;
            case EXP:
                h = expon(x, p);
                break;
            case LOG:
                h = logar(x, p);
                break;
        }
        //System.out.println(h);
        return h;
    }

    private double logar(double x, Ansver p) {
        return p.a*Math.log(x) + p.b;
    }

    private double expon(double x, Ansver p) {
        return p.a*Math.exp(x*p.b);
    }

    private double stage(double x, Ansver p) {
        return p.a*Math.pow(x, p.b);
    }

    private double line(double x, Ansver p) {
        return p.a*x + p.b;
    }

    private double rect(double x, Ansver p) {
        return p.a*Math.pow(x, 2) + p.b*x + p.c;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static double mod(double x){
        if(x >= 0){
            return x;
        }else{
            return -1*x;
        }
    }

    private double max(double a, double b){
        if(a > b){
            return a;
        }else{
            return b;
        }
    }
}
