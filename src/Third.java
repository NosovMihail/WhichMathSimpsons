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
import javafx.stage.Stage;

public class Third extends Application {

    private int selectedFunk = 0;
    private int selectedMethod = 0;
    private int i = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Лаба 3");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        ObservableList<String> funcs = FXCollections.observableArrayList("2x^3 - 5*x^2 - 3x + 21", "sin(x)/x", "5/x");
        ComboBox<String> funcComboBox = new ComboBox<String>(funcs);
        funcComboBox.setValue("2x^3 - 5*x^2 - 3x + 21");

        ObservableList<String> method = FXCollections.observableArrayList("Прямоугольники левые", "Прямоугольники правые", "Прямоугольники средние", "Трапеции", "Симпсона");
        ComboBox<String> methodComboBox = new ComboBox<String>(method);
        methodComboBox.setValue("Прямоугольники левые");

        TextField leftBorder = new TextField("-5");
        TextField rightBorder = new TextField("5");
        FlowPane drawBorders = new FlowPane(new Label("Область отрисовки "), leftBorder, rightBorder);

        TextField aBorder = new TextField("-2");
        TextField bBorder = new TextField("5");
        FlowPane borders = new FlowPane(new Label("Область поиска ответов "), aBorder, bBorder);

        TextField field = new TextField("4");
        FlowPane text = new FlowPane(new Label("Точность "), field);

        Button selectBtn = new Button("Вычислить");

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x,y);

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
        pane.add(funcComboBox, 0,1);
        pane.add(methodComboBox, 0,2);
        //pane.add(funk2, 0,2);
        pane.add(borders, 1, 1);
        GridPane.setColumnSpan(borders, 2);
        pane.add(drawBorders, 1, 2);
        GridPane.setColumnSpan(drawBorders, 2);
        pane.add(text, 0, 3);
        pane.add(selectBtn, 1,3);
        pane.add(ans, 2,3);
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
                double tochnost = 4;
                try {
                    tochnost = Double.parseDouble(field.getText());
                }catch (Exception e){}
                //selectedLbl.setText("Selected: " + selection.getText());
                if(methodComboBox.getValue().equals("Прямоугольники правые")){
                    selectedMethod = 1;
                }else if(methodComboBox.getValue().equals("Прямоугольники средние")) {
                    selectedMethod = 2;
                }else if(methodComboBox.getValue().equals("Прямоугольники левые")) {
                    selectedMethod = 0;
                }else if(methodComboBox.getValue().equals("Трапеции")) {
                    selectedMethod = 3;
                }else if(methodComboBox.getValue().equals("Симпсона")){
                    selectedMethod = 4;
                }
                if(funcComboBox.getValue().equals("sin(x)/x")){
                    selectedFunk = 1;
                }else if(funcComboBox.getValue().equals("5/x")) {
                    selectedFunk = 2;
                }else{
                    selectedFunk = 0;
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
                for(double i = n; i<m; i += (m-n)/1000){
                    datas.add(new XYChart.Data(i,function(i)));

                    //datas2.add(new XYChart.Data(i,Math.cos(i)));
                }
                double a = -2;
                double b = 5;
                try{
                    a = Double.parseDouble(aBorder.getText());
                    b = Double.parseDouble(bBorder.getText());
                    if(b < a){
                        double temp = a;
                        a = b;
                        b = temp;
                    }
                }catch (Exception e){}
                double p = 0;
                if(selectedMethod == 0) {
                    try {
                        p = leftRect(a, b, datas2, tochnost, m, n);
                        ans.setText("Результат: " + p);
                    } catch (Exception e) {
                        datas2.add(new XYChart.Data(a, function(a)));
                        ans.setText("Уточните границы поиска");
                    }
                }else if(selectedMethod == 1){
                    try {
                        p = rightRect(a, b, datas2, tochnost, m, n);
                        ans.setText("Результат: " + p);
                    } catch (Exception e) {
                        ans.setText("Уточните границы поиска");
                    }
                }else if(selectedMethod == 2){
                    try {
                        p = centerRect(a, b, datas2, tochnost, m, n);
                        ans.setText("Результат: " + p);
                    } catch (Exception e) {
                        ans.setText("Уточните границы поиска");
                    }
                }else if(selectedMethod == 3){
                    try {
                        p = trp(a, b, datas2, tochnost, m, n);
                        ans.setText("Результат: " + p);
                    } catch (Exception e) {
                        ans.setText("Уточните границы поиска");
                    }
                }else{
                    try {
                        p = simps(a, b, datas2, tochnost, m, n);
                        ans.setText("Результат: " + p);
                    } catch (Exception e) {
                        ans.setText("Уточните границы поиска");
                    }
                }
                //System.out.println(p + " "  + function(p));

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

    private double function(double p) {
        if(Math.round(p * Math.pow(10, 9))/Math.pow(10, 9) == Math.round(p)){
            p = Math.round(p);
        }
        double h = 0;
        if(selectedFunk == 0){
            h = func1(p);
        }else{
            if(selectedFunk == 1) {
                h = func2(p);
            }else{
                h = func3(p);
            }
        }
        System.out.println(h + " " + p);
        if(Double.isFinite(h)) {
            return h;
        }else{
            return (function(p + 0.001)+function(p - 0.001))/2;
        }
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

    public double leftRect(double a, double b, ObservableList<XYChart.Data> datas2, double l, double m, double n) throws Exception {
        double x = a;
        i = 1;
        double sum = 0;
        //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        for(int j = 0; j < l; j++){
            if((x > n)&&(x < m)) {
                datas2.add(new XYChart.Data(x, function(x)));
            }
            i++;
            sum += (b-a)/l*function(x);
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x + (b-a)/l, function(x)));
            }
            x += (b-a)/l;
            //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        }
        return sum;
    }

    public double rightRect(double a, double b, ObservableList<XYChart.Data> datas2, double l, double m, double n) throws Exception {
        double x = b;
        i = 1;
        double sum = 0;
        //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        for(int j = 0; j < l; j++){
            if((x > n)&&(x < m)) {
                datas2.add(new XYChart.Data(x, function(x)));
            }
            i++;
            sum += (b-a)/l*function(x);
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x - (b-a)/l, function(x)));
            }
            x -= (b-a)/l;
            //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        }
        return sum;
    }

    public double centerRect(double a, double b, ObservableList<XYChart.Data> datas2, double l, double m, double n) throws Exception {
        double x = a;
        i = 1;
        double sum = 0;
        //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        for(int j = 0; j < l; j++){
            if((x > n)&&(x < m)) {
                datas2.add(new XYChart.Data(x, function(x + (b-a)/(l*2))));
            }
            i++;
            sum += (b-a)/l*function(x + (b-a)/l/2);
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x + (b-a)/l, function(x + (b-a)/(l*2))));
            }
            x += (b-a)/l;
            //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        }
        return sum;
    }

    private double trp(double a, double b, ObservableList<XYChart.Data> datas2, double l, double m, double n) {
        double x = a;
        i = 1;
        double sum = 0;
        //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        for(int j = 0; j < l; j++){
            if((x > n)&&(x < m)) {
                datas2.add(new XYChart.Data(x, function(x)));
            }
            i++;
            sum += (b-a)/l*(function(x)+function(x + (b-a)/l))/2;
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x + (b-a)/l, function(x + (b-a)/l)));
            }
            x += (b-a)/l;
            //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        }
        return sum;
    }

    private double simps(double a, double b, ObservableList<XYChart.Data> datas2, double l, double m, double n) {
        double x = a;
        i = 1;
        double sum = 0;
        //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        for(int j = 0; j < l; j++){
            if((x > n)&&(x < m)) {
                datas2.add(new XYChart.Data(x, function(x)));
            }
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x + (b-a)/(2*l), function(x + (b-a)/(2*l))));
            }
            if((x > n)&&(x < m)){
                datas2.add(new XYChart.Data(x + (b-a)/l, function(x + (b-a)/l)));
            }
            i++;
            sum += (b-a)/(6*l)*(function(x) + 4*function(x + (b-a)/(2*l)) + function(x + (b-a)/l));
            x += (b-a)/l;
            //System.out.println(a + "\t" + b + "\t" + x + "\t" + function(a) + "\t" + function(b) + "\t" + function(x) + "\t" + mod(a - b));
        }
        return sum;
    }

    private double max(double a, double b){
        if(a > b){
            return a;
        }else{
            return b;
        }
    }

    private double functionA(double p) {
        if(selectedFunk == 0){
            return funcA1(p);
        }else{
            if(selectedFunk == 1) {
                return funcA2(p);
            }else{
                return funcA3(p);
            }
        }
    }

    private double funcA1(double p) {
        return Math.pow(p, 2)*4.86 - 16.3*p + 4.39;
    }

    private double funcA2(double p) {
        return Math.cos(p);
    }

    private double funcA3(double p) {
        return Math.pow(p, 4)*15;
    }

    public static double func1(double x){
        return Math.pow(x, 3)*2 - 5*Math.pow(x, 2) - 3*x + 21;
    }

    public static double func2(double p) {
        return Math.sin(p)/p;
    }

    public static double func3(double p) {
        return 5/p;
    }
}
