package com.company;

import java.util.concurrent.CountDownLatch;

public class SingleThread  implements Runnable{
    private final int[][] result;
    private int[][] matrix1;
    private int[][] matrix2;
    private final int row;
    private CountDownLatch latch;

    public SingleThread(int[][] result, int[][] matrix1, int[][] matrix2, int row) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
    }

    public SingleThread(int[][] result, int[][] matrix1, int[][] matrix2, int row, CountDownLatch latch) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
        this.latch = latch;
    }

    @Override
    public void run() {
        for(int i = 0;i<matrix2[0].length;i++){
            for (int j = 0; j < matrix1[row].length; j++) {
                result[row][i] += matrix1[row][j] * matrix2[j][i];
            }
        }
        if(latch!=null) {
            latch.countDown();
        }
    }
}

/**
 *                   1 2
 * 1 2 3             3 4  = (1+6+21) +      (1*3) * (3*2) = 1 * 2
 * (1*3)             7 8
 * **/