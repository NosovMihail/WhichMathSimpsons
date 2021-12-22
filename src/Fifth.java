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

public class Fifth extends Application {

    private int selectedFunk = 0;
    private int selectedMethod = 0;
    private int i = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Лаба 5");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        ObservableList<String> funcs = FXCollections.observableArrayList("2x^3 - 5*x^2 - 3x + 21", "sin(x)", "5/x");
        ComboBox<String> funcComboBox = new ComboBox<String>(funcs);
        funcComboBox.setValue("2x^3 - 5*x^2 - 3x + 21");

        ObservableList<String> method = FXCollections.observableArrayList("Лагранж", "Ньютон с разд. разн.", "Ньютон с кон. разн.");
        ComboBox<String> methodComboBox = new ComboBox<String>(method);
        methodComboBox.setValue("Лангранж");

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
        Button funkBtn = new Button("Применить");
        funkBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String[] tempLines = dots.getText().split("\n");
                double x = Double.parseDouble(tempLines[0]);
                double n = -5;
                double m = 5;
                dots.clear();
                dots.appendText(x + "\n");
                try{
                    n = Double.parseDouble(leftBorder.getText());
                    m = Double.parseDouble(rightBorder.getText());
                    if(m < n){
                        double temp1 = m;
                        m = n;
                        n = temp1;
                    }
                }catch (Exception e){}
                if(m-n < 1){
                    m = 5;
                    n = -5;
                }
                for(long i = Math.round(n); i < Math.round(m)+1; i++) {
                    if (funcComboBox.getValue().equals("sin(x)")) {
                        if(i != 0)
                        dots.appendText(i + " " + func2(i) + "\n");
                    } else if (funcComboBox.getValue().equals("5/x")) {
                        if(i != 0)
                        dots.appendText(i + " " + func3(i) + "\n");
                    } else {
                        dots.appendText(i + " " + func1(i) + "\n");
                    }
                }
            }
        });
        Button selectBtn = new Button("Вычислить");

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x,y);
        numberScatterChart.setAnimated(false);

        Label ans = new Label("Ответ");
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
        pane.add(funcComboBox, 0,3);
        pane.add(funkBtn, 0,4);
        pane.add(borders, 1, 1);
        GridPane.setColumnSpan(borders, 2);
        pane.add(drawBorders, 1, 2);
        GridPane.setColumnSpan(drawBorders, 2);
        pane.add(selectBtn, 1,3);
        pane.add(ans, 2,3);
        numberScatterChart.setTitle("График");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();

        series2.setName("Результат");
        series1.setName("Точки");
        series3.setName("Интерполяция");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();
        selectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                datas.clear();
                datas2.clear();
                datas3.clear();
                switch (methodComboBox.getValue()){
                    case "Лагранж":
                        selectedMethod = 0;
                        break;
                    case "Ньютон с разд. разн.":
                        selectedMethod = 1;
                        break;
                    case "Ньютон с кон. разн.":
                        selectedMethod = 2;
                        break;
                }
                String[] tempLines = dots.getText().split("\n");
                double x = Double.parseDouble(tempLines[0]);
                double[][] dotValues = new double[2][tempLines.length - 1];
                for(int i = 0; i < tempLines.length - 1; i++){
                    String[] oneLine = tempLines[i + 1].split(" ");
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
                double p;
                switch (selectedMethod){
                    case 0:
                        p = lagr(x, dotValues, datas3);
                        for(double i = n; i < m; i += (m - n)/1000){
                            //System.out.println(p.a + " " + p.b + " " + i);
                            datas3.add(new XYChart.Data(i, lagr(i, dotValues, datas3)));
                        }
                        break;
                    case 1:
                        p = nutonA(x, dotValues, datas3);
                        for(double i = n; i < m; i += (m - n)/1000){
                            //System.out.println(p.a + " " + p.b + " " + i);
                            datas3.add(new XYChart.Data(i, nutonA(i, dotValues, datas3)));
                        }
                        break;
                    case 2:
                        p = nutonB(x, dotValues, datas3);
                        for(double i = n; i < m; i += (m - n)/1000){
                            //System.out.println(p.a + " " + p.b + " " + i);
                            datas3.add(new XYChart.Data(i, nutonB(i, dotValues, datas3)));
                        }
                        break;
                    default:
                        p = 0;
                        break;
                }
                ans.setText("Ответ: " + p);
                datas2.add(new XYChart.Data(x, p));
                for(int i = 0; i < dotValues[0].length; i += 1){
                    if((dotValues[0][i] > n)&&(dotValues[0][i] < m)) {
                        datas.add(new XYChart.Data(dotValues[0][i], dotValues[1][i]));
                    }
                    //datas2.add(new XYChart.Data(i,Math.cos(i)));
                }
                series3.setData(datas3);
                series1.setData(datas);
                series2.setData(datas2);
            }
        });

        datas.add(new XYChart.Data(0,0));
        datas2.add(new XYChart.Data(0,0));
        datas3.add(new XYChart.Data(0,0));
        series1.setData(datas);
        series2.setData(datas2);
        series3.setData(datas3);
        Scene scene = new Scene(pane, 1200,800);
        scene.getStylesheets().add("Succ.css");
        numberScatterChart.getData().add(series1);
        numberScatterChart.getData().add(series2);
        numberScatterChart.getData().add(series3);
        primaryStage.setScene(scene);

        primaryStage.show();


    }

    private double lagr(double x, double[][] dotValues, ObservableList<XYChart.Data> datas3) {
        double ans = 0;
        for(int i = 0; i < dotValues[0].length; i++){
            double part = 1;
            for(int j = 0; j < dotValues[0].length; j++) {
                if (j != i) {
                    part *= (x - dotValues[0][j]) / (dotValues[0][i] - dotValues[0][j]);
                }
            }
            ans += part*dotValues[1][i];
        }
        return ans;
    }

    private double nutonA(double x, double[][] dotValues, ObservableList<XYChart.Data> datas3) {
        double ans = 0;
        ans += dotValues[1][0];
        double[][] f = new double[dotValues[0].length][dotValues[0].length];
        for(int l = 0; l < f.length; l++){
            f[0][l] = dotValues[1][l];
        }
        for(int l = 1; l < f.length; l++){
            for(int m = 0; m < f.length-l; m++){
                f[l][m] = (f[l-1][m+1] - f[l-1][m])/(dotValues[0][m+l] - dotValues[0][m]);
                System.out.println(f[l][m] + " " + dotValues[0][m+l] + " " + dotValues[0][m]);
            }
            System.out.println();
        }
        for(int k = 1; k < dotValues[0].length; k++){
            double s = 1;
            for(int i = 0; i < k; i++){
                s *= (x-dotValues[0][i]);
            }
            ans += f[k][0]*s;
        }
        return ans;
    }

    private double nutonB(double x, double[][] dotValues, ObservableList<XYChart.Data> datas3) {
        double ans = 0;
        double[][] f = new double[dotValues[0].length][dotValues[0].length];
        for(int l = 0; l < f.length; l++){
            f[0][l] = dotValues[1][l];
        }
        int m = 0;
        int mStart = 0;
        for(int l = 0; l < f.length - 1; l++){
            if(x >= dotValues[0][l] && x < dotValues[0][l+1]){
                m = l;
                mStart = l;
            }
        }
        //System.out.println(m);
        ans += dotValues[1][mStart];
        for(int l = 1; l < f.length; l++){
            for(; m < f.length-l; m++){
                f[l][m] = (f[l-1][m+1] - f[l-1][m]);
                //System.out.println(f[l][m] + " " + dotValues[0][m+l] + " " + dotValues[0][m]);
            }
            //System.out.println();
        }
        for(int k = 1; k < dotValues[0].length - mStart; k++){
            double s = (x - dotValues[0][mStart])/(dotValues[0][1] - dotValues[0][0]);
            for(int i = 1; i < k; i++){
                s *= (s-i)/i;
            }
            ans += f[k][mStart]*s;
        }
        return ans;
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
        //System.out.println(minD);
        for(int i = 1; i < best.length; i++){
            double D = calculateD(best[i], dotValues);
            //System.out.println(D);
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
        //System.out.println(sum);
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
        return new Ansver(Type.LINEAR, a, b);
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

    public static double func1(double x){
        return Math.pow(x, 3)*2 - 5*Math.pow(x, 2) - 3*x + 21;
    }

    public static double func2(double p) {
        return Math.sin(p);
    }

    public static double func3(double p) {
        return 5/p;
    }
}
