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

public class Six extends Application {

    private int selectedFunk = 0;
    private int selectedMethod = 0;
    private int i = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Лаба 3");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        ObservableList<String> funcs = FXCollections.observableArrayList("y' = y + (x+1)y^2", "y' = -2y", "y' = -(2y + 1)*ctg(x)");
        ComboBox<String> funcComboBox = new ComboBox<String>(funcs);
        funcComboBox.setValue("y' = y + (x+1)y^2");

        ObservableList<String> method = FXCollections.observableArrayList("Эйлера", "Адамса");
        ComboBox<String> methodComboBox = new ComboBox<String>(method);
        methodComboBox.setValue("Эйлера");

        TextField leftBorder = new TextField("0.5");
        TextField rightBorder = new TextField("5");
        FlowPane drawBorders = new FlowPane(new Label("Область отрисовки "), leftBorder, rightBorder);

        TextField aBorder = new TextField("1");
        TextField bBorder = new TextField("1.5");
        FlowPane borders = new FlowPane(new Label("[a, b]"), aBorder, bBorder);

        TextField field = new TextField("0.1");
        FlowPane text = new FlowPane(new Label("h "), field);
        TextField field2 = new TextField("-1");
        FlowPane text2 = new FlowPane(new Label("y(a)"), field2);

        Button selectBtn = new Button("Вычислить");

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x,y);

        TextArea ans = new TextArea("Ответ");
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
        pane.add(text2, 1, 3);
        pane.add(selectBtn, 2,3);
        pane.add(ans, 0,4);
        GridPane.setColumnSpan(ans, 3);
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
                double y0 = 1;
                try {
                    tochnost = Double.parseDouble(field.getText());
                }catch (Exception e){}
                try {
                    y0 = Double.parseDouble(field2.getText());
                }catch (Exception e){}
                //selectedLbl.setText("Selected: " + selection.getText());
                if(methodComboBox.getValue().equals("Адамса")){
                    selectedMethod = 1;
                }else if(methodComboBox.getValue().equals("Эйлера")) {
                    selectedMethod = 0;
                }
                if(funcComboBox.getValue().equals("y' = -2y")){
                    selectedFunk = 1;
                }else if(funcComboBox.getValue().equals("5/x")) {
                    selectedFunk = 2;
                }else{
                    selectedFunk = 0;
                }
                double n = 0.5;
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
                double con = functionC(a, y0);
                for(double i = n; i<m; i += (m-n)/1000){
                    datas.add(new XYChart.Data(i,functionA(i, con)));

                    //datas2.add(new XYChart.Data(i,Math.cos(i)));
                }
                double p = 0;
                if(selectedMethod == 0) {
                    try {
                        eiler(a, b, datas2, tochnost, y0, m, n, con, ans);
                    } catch (Exception e) {
                        ans.setText("Уточните границы поиска");
                    }
                }else if(selectedMethod == 1){
                    try {
                        adams(a, b, datas2, tochnost, y0, m, n, con, ans);
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

    private double functionC(double p, double y) {
        if(Math.round(p * Math.pow(10, 9))/Math.pow(10, 9) == Math.round(p)){
            p = Math.round(p);
        }
        double h = 0;
        if(selectedFunk == 0){
            h = funcC1(p, y);
        }else{
            if(selectedFunk == 1) {
                h = funcC2(p, y);
            }else{
                h = funcC3(p, y);
            }
        }
        //System.out.println(h + " " + p);
        if(Double.isFinite(h)) {
            return h;
        }else{
            return (functionC(p + 0.001, y)+functionC(p - 0.001, y))/2;
        }
    }

    private double funcC3(double p, double y) {
        return 0;
    }

    private double funcC2(double p, double y) {
        return y/Math.exp(-2*p);
    }

    private void adams(double a, double b, ObservableList<XYChart.Data> datas2, double h, double y0, double m, double n, double c, TextArea ans) {
        int i = 0;
        double yY1 = 0;
        double yY2 = 0;
        double yY3 = y0;
        double y1 = 0;
        double y2 = 0;
        double y3 = y0;
        double R = 0;
        double yY = y0;
        h = h/2;
        String answer = "Шаг\tx\t\ty*\t\ty\t\tR\n";
        datas2.add(new XYChart.Data(a, y0));
        answer += String.format("%s\t%.5f\t%.5f\t%.5f\n", i, a, y0, functionA(a, c));
        while(i < 3){
            i++;
            y0 = y0 + 2*h*(function(a, y0));
            y1 = y2;
            y2 = y3;
            y3 = y0;
            a += 2*h;
            datas2.add(new XYChart.Data(a, y0));
            answer += String.format("%s\t%.5f\t%.5f\t%.5f\n", i, a, y0, functionA(a, c));
        }
        int i2 = i;
        while(a < b){
            System.out.println("1");
            i++;
            if(i%2 == 0) {
                i2++;
                System.out.println("2");
                y0 = y0 + 2*h * function(a, y0) + Math.pow(2*h, 2) / 2 * function(a, y3) + 5 / 12 * Math.pow(2*h, 3) * function(a, y2) + 3 / 8 * Math.pow(2*h, 4) * function(a, y1);
                yY = yY + h * function(a, yY) + Math.pow(h, 2) / 2 * function(a, yY3) + 5 / 12 * Math.pow(h, 3) * function(a, yY2) + 3 / 8 * Math.pow(h, 4) * function(a, yY1);
                yY1 = yY2;
                yY2 = yY3;
                yY3 = yY;
                y1 = y2;
                y2 = y3;
                y3 = y0;
                a += h;
                datas2.add(new XYChart.Data(a, y0));
                R = (y0 - yY)/15;
                answer += String.format("%s\t%.5f\t%.5f\t%.5f\t%.5f\n", i2, a, y0, functionA(a, c), R);
            }else{
                yY = yY + h * function(a, yY) + Math.pow(h, 2) / 2 * function(a, yY3) + 5 / 12 * Math.pow(h, 3) * function(a, yY2) + 3 / 8 * Math.pow(h, 4) * function(a, yY1);
                yY1 = yY2;
                yY2 = yY3;
                yY3 = yY;
                a += h;
            }
        }
        ans.setText(answer);
    }

    private void eiler(double a, double b, ObservableList<XYChart.Data> datas2, double h, double y0, double m, double n, double c, TextArea ans) {
        int i = 0;
        String answer = "Шаг\tx\t\ty*\t\ty\t\tR\n";
        answer += String.format("%s\t%.5f\t%.5f\t%.5f\n", i, a, y0, functionA(a, c));
        h = h/2;
        double y1 = y0;
        double R = 0;
        int i2 = i;
        while(a < b){
            i++;
            if(i%2 == 0) {
                i2++;
                y0 = y0 + 2*h * (function(a, y0));
                y1 = y1 + h * (function(a, y1));
                a += h;
                datas2.add(new XYChart.Data(a, y0));
                R = (y0 - y1);
                answer += String.format("%s\t%.5f\t%.5f\t%.5f\t%.5f\n", i2, a, y0, functionA(a, c), R);
            }else{
                y1 = y1 + h * (function(a, y1));
                a += h;
            }
        }
        ans.setText(answer);
    }

    private double function(double p, double y) {
        if(Math.round(p * Math.pow(10, 9))/Math.pow(10, 9) == Math.round(p)){
            p = Math.round(p);
        }
        double h = 0;
        if(selectedFunk == 0){
            h = func1(p, y);
        }else{
            if(selectedFunk == 1) {
                h = func2(p, y);
            }else{
                h = func3(p, y);
            }
        }
        //System.out.println(h + " " + p);
        if(Double.isFinite(h)) {
            return h;
        }else{
            return (function(p + 0.001, y)+function(p - 0.001, y))/2;
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

    private double max(double a, double b){
        if(a > b){
            return a;
        }else{
            return b;
        }
    }

    private double functionA(double p, double c) {
        if(selectedFunk == 0){
            return funcA1(p, c);
        }else{
            if(selectedFunk == 1) {
                return funcA2(p, c);
            }else{
                return funcA3(p, c);
            }
        }
    }

    private double funcA1(double p, double c) {
        return -1*(Math.exp(p)/(c + Math.exp(p)*p));
    }

    private double funcA2(double p, double c) {
        return c*Math.exp(-2*p);
    }

    private double funcA3(double p, double c) {
        return c/Math.pow(Math.sin(p), 2)/2 + 1/2;
    }

    public static double funcC1(double a, double y0){
        return -1*(Math.exp(a)/y0) - Math.exp(a)*a;
    }

    public static double func1(double x, double y){
        return Math.pow(y, 2)*(x + 1) + y;
    }

    public static double func2(double p, double y) {
        return -2*y;
    }

    public static double func3(double p, double y) {
        return -1 * (2*y + 1)*Math.cos(p)/Math.sin(p);
    }
}
