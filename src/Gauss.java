public class Gauss {
    public static Ansver calculate(Type tp, int n, double[][] a, double[] b){
        double[] x = new double[n];
        for(int i = 0; i < n - 1; i++){
            if(a[i][i] == 0) {  //если "первый" коэффициент в строке равен нулю
                boolean notZeroExists = false;
                for (int j = i + 1; j < n; j++) { //ищем строку с ненулевым
                    if (a[j][i] != 0){
                        a = swapA(a, i, j);  //меняем местами строки
                        b = swapB(b, i, j);
                        notZeroExists = true;
                        break;
                    }
                }
                //if(!notZeroExists){
                //    throw new badMatrixException(); //если не нашли, значит система несовмеснтна, кидаем ошибку ввода
                //}
            }
            for(int k = i + 1; k < n; k++){
                double c = a[k][i]/a[i][i]; //коэффициент для вычитания строк
                a[k][i] = 0;
                for(int j = i + 1; j < n; j++){
                    a[k][j] -= c*a[i][j]; //вычитание строк
                }
                b[k] -= c*b[i];
            }
        }
        //if(a[n-1][n-1] == 0){ // проверяем, что единственный коэффициент в последней строке не ноль
        //    throw new badMatrixException();
        //}
        x[n-1] = b[n-1]/a[n-1][n-1]; //вычисляем неизвестную в последней строке
        for(int i = n - 2; i >= 0; i--){
            double s = 0;
            for(int j = i + 1; j < n; j++){
                s += a[i][j]*x[j]; //сумма очередной строки
            }
            x[i] = (b[i] - s)/a[i][i]; //единственная оставшаяся неизвестная в строке
        }
        Ansver answer = new Ansver(tp, x[2], x[1], x[0]);
        return answer;
    }

    private static  double[][] swapA(double[][] a, int i, int j){
        double[] line = a[i];
        a[i] = a[j];
        a[j] = line;
        return a;
    }

    private static  double[] swapB(double[] b, int i, int j){
        double l = b[i];
        b[i] = b[j];
        b[j] = l;
        return b;
    }
}
