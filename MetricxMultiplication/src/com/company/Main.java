package com.company;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Generate el metrics
        int[][] matrix1 = matrixGeneration(15, 15);
        int[][] matrix2 = matrixGeneration(15, 15);
        printMatrix(matrix1);
        System.out.println("=======================================");
        printMatrix(matrix2);

        System.out.println("============Mul=================");
        int[][] mulResult = mul(matrix1, matrix2);
        printMatrix(mulResult);


        System.out.println("============Mul Using Threads=================");
         mulResult = mulUsingThreads(matrix1, matrix2);
        printMatrix(mulResult);
    }

    private static int[][] mul(int[][] matrix1, int[][] matrix2) {
        int numOfRows = matrix1.length;
        int numOfCols = matrix2[0].length;
        int[][] result = new int[numOfRows][numOfCols];

        int columns2 = matrix2[0].length;

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < columns2; j++) {
                for (int k = 0; k < numOfCols; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

    private static int[][] mulUsingThreads(int[][] matrix1, int[][] matrix2) throws InterruptedException {
        int numOfRows = matrix1.length;
        int numOfCols = matrix2[0].length;
        int[][] result = new int[numOfRows][numOfCols];

        for (int i = 0; i < numOfRows; i++) {
            SingleThread singleThread = new SingleThread(result, matrix1, matrix2, i);
            printMatrix(result);
            Thread thread = new Thread(singleThread);
            thread.start();
            thread.join();
        }

        return result;
    }


    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        // print el metrics
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static int[][] matrixGeneration(int rows, int columns) {

        int[][] output = new int[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                output[i][j] = random.nextInt(100);
            }
        }

        return output;
    }
}
