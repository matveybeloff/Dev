package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.example.Service.CsvToDatabaseLoader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class SchoolDataAnalysisApp extends JFrame {
    private static EntityManagerFactory emf;

    public SchoolDataAnalysisApp(String title) {
        super(title);
    }

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("SchoolUnit");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1 - Построить график по среднему количеству студентов в 10 округах");
            System.out.println("2 - Вывести среднее количество расходов для заданных округов");
            System.out.println("3 - Найти учебное заведение с наивысшим показателем по математике в заданных диапазонах студентов");
            System.out.println("4 - Перезаписать данные из CSV файла в базу данных");
            System.out.println("0 - Выйти");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> buildAverageStudentsChart();
                case 2 -> printAverageExpenditure();
                case 3 -> findHighestMathScoreSchool();
                case 4 -> CsvToDatabaseLoader.loadCsvDataToDatabase("Школы.csv");
                case 0 -> {
                    emf.close();
                    System.exit(0);
                }
                default -> System.out.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    // Задание 1: Построить график по среднему количеству студентов в 10 округах
    private static void buildAverageStudentsChart() {
        EntityManager em = emf.createEntityManager();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String queryString = "SELECT d.county, AVG(s.students) FROM DistrictData d JOIN d.schools sc JOIN sc.statistics s "
                + "GROUP BY d.county ORDER BY AVG(s.students) DESC";

        List<Object[]> results = em.createQuery(queryString).setMaxResults(10).getResultList();
        for (Object[] row : results) {
            String county = (String) row[0];
            Double avgStudents = (Double) row[1];
            dataset.addValue(avgStudents, "Students", county);
        }
        em.close();

        JFreeChart chart = ChartFactory.createBarChart(
                "Среднее количество студентов по округам", "Округа", "Количество студентов",
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true, false);

        SchoolDataAnalysisApp chartApp = new SchoolDataAnalysisApp("Среднее количество студентов");
        chartApp.setContentPane(new ChartPanel(chart));
        chartApp.pack();
        chartApp.setVisible(true);
    }

    // Задание 2: Вывести среднее количество расходов для заданных округов
    private static void printAverageExpenditure() {
        EntityManager em = emf.createEntityManager();
        String queryString = "SELECT d.county, AVG(s.expenditure) FROM DistrictData d JOIN d.schools sc JOIN sc.statistics s "
                + "WHERE d.county IN ('Fresno', 'Contra Costa', 'El Dorado', 'Glenn') AND s.expenditure > 10 "
                + "GROUP BY d.county";

        Query query = em.createQuery(queryString);
        List<Object[]> results = query.getResultList();

        System.out.println("Средние расходы в округах (expenditure > 10):");
        for (Object[] row : results) {
            String county = (String) row[0];
            Double avgExpenditure = (Double) row[1];
            System.out.printf("Округ: %s, Средние расходы: %.2f\n", county, avgExpenditure);
        }
        em.close();
    }

    // Задание 3: Найти учебное заведение с наивысшим показателем по математике в заданных диапазонах студентов
    private static void findHighestMathScoreSchool() {
        EntityManager em = emf.createEntityManager();
        String queryString = "SELECT sc.name, s.students, s.math FROM SchoolData sc JOIN sc.statistics s "
                + "WHERE (s.students BETWEEN 5000 AND 7500 OR s.students BETWEEN 10000 AND 11000) "
                + "ORDER BY s.math DESC";

        Query query = em.createQuery(queryString).setMaxResults(1);
        List<Object[]> results = query.getResultList();

        System.out.println("Учебное заведение с самым высоким показателем по математике:");
        for (Object[] row : results) {
            String schoolName = (String) row[0];
            int students = (int) row[1];
            double mathScore = (double) row[2];
            System.out.printf("Школа: %s, Студенты: %d, Показатель по математике: %.2f\n", schoolName, students, mathScore);
        }
        em.close();
    }
}