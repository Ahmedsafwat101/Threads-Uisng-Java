package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // Generate el metrics row2 * col1  output -> row1 * col2
        int[][] matrix1 = matrixGeneration(15, 3);
        int[][] matrix2 = matrixGeneration(3, 3);
        printMatrix(matrix1);
        System.out.println("=======================================");
        printMatrix(matrix2);

        System.out.println("============Mul=================");
       int[][] mulResult1 = mul(matrix1, matrix2);
        printMatrix(mulResult1);

        System.out.println("============Mul Using Threads=================");
        int[][] mulResult2 = mulUsingThreads(matrix1, matrix2);
        printMatrix(mulResult2);

        System.out.println("============Mul Using Threads with fixed size =================");
        int[][] mulResult3 = mulUsingThreadsFixedNumberOfThreads(matrix1, matrix2,3);
        printMatrix(mulResult3);

    }

    private static int[][] mul(int[][] matrix1, int[][] matrix2) {
        int row1 = matrix1.length;
        int col2 = matrix2[0].length;

        int[][] result = new int[row1][col2];

        int row2 = matrix2.length;

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for ( int k = 0; k < row2; k++)
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
            }
        }

        return result;
    }

    private static int[][] mulUsingThreads(int[][] matrix1, int[][] matrix2) throws InterruptedException {
        int numOfRows = matrix1.length;
        int numOfCols = matrix2[0].length;
        int[][] result = new int[numOfRows][numOfCols];
        List<Object> threads = new ArrayList<>();


        for (int i = 0; i < numOfRows; i++) {
            SingleThread singleThread = new SingleThread(result, matrix1, matrix2, i);
            printMatrix(result);
            Thread thread = new Thread(singleThread);
            thread.start();

            threads.add(thread);

            if (threads.size() % 10 == 0) {
                release(threads);
            }
        }

        return result;
    }


    /*private static int[][] mulUsingThreadsFixedNumberOfThreads(int[][] matrix1, int[][] matrix2) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        int numOfRows = matrix1.length;
        int numOfCols = matrix2[0].length;
        CountDownLatch latch = new CountDownLatch(numOfRows);

        int[][] result = new int[numOfRows][numOfCols];

        for (int i = 0; i < numOfRows; i++) {
            SingleThread singleThread = new SingleThread(result, matrix1, matrix2, i, latch);
            printMatrix(result);
            Thread thread = new Thread(singleThread);
            executor.submit(thread);
        }


        latch.await();

        executor.isShutdown();


        return result;
    }*/


    private static int[][] mulUsingThreadsFixedNumberOfThreads(int[][] matrix1, int[][] matrix2, int threadNum) throws Exception {
        List<Thread>threadList = new ArrayList<>();
        int numOfRows = matrix1.length;
        int numOfCols = matrix2[0].length;
        int[][] result = new int[numOfRows][numOfCols];

        for (int i = 0; i < numOfRows; i++) {

            SingleThread singleThread = new SingleThread(result, matrix1, matrix2, i);
            printMatrix(result);
            Thread thread = new Thread(singleThread);
            thread.start();

            threadList.add(thread);

            System.out.println(Thread.activeCount());

            if (threadList.size() % threadNum == 0) {
                release(threadList);
            }
        }
        return result;
    }

    private static void release(List threads) {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for(Thread t :threadSet)
            System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon());

        for (Object thread : threads) {
            try {

                ((Thread) thread).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }

    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        // print el metrics
        for (int[] ints : matrix) {
            for (int j = 0; j < columns; j++) {
                System.out.print(ints[j] + "  ");
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
