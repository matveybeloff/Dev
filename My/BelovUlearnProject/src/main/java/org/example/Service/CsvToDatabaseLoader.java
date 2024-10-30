package org.example.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.Entity.DistrictData;
import org.example.Entity.SchoolData;
import org.example.Entity.StatisticsData;

import java.io.FileReader;
import java.io.IOException;

public class CsvToDatabaseLoader {

    public static void loadCsvDataToDatabase(String csvFilePath) {
        EntityManager em = DatabaseManager.getEntityManager();
        EntityTransaction transaction = em.getTransaction();


        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            transaction.begin();
            em.createQuery("DELETE FROM StatisticsData").executeUpdate();
            em.createQuery("DELETE FROM SchoolData").executeUpdate();
            em.createQuery("DELETE FROM DistrictData").executeUpdate();

            String[] header = reader.readNext();  // Пропускаем заголовки
            String[] line;

            while ((line = reader.readNext()) != null) {
                // Создаем объект District
                String[] finalLine = line;
                DistrictData district = em.createQuery("SELECT d FROM DistrictData d WHERE d.districtCode = :code", DistrictData.class)
                        .setParameter("code", line[1])
                        .getResultStream()
                        .findFirst()
                        .orElseGet(() -> {
                            DistrictData newDistrict = new DistrictData();
                            newDistrict.setDistrictCode(finalLine[1]);
                            newDistrict.setCounty(finalLine[3]);
                            em.persist(newDistrict);
                            return newDistrict;
                        });

                // Создаем объект School
                SchoolData school = new SchoolData();
                school.setName(line[2]);
                school.setGrades(line[4]);
                school.setDistrict(district);
                em.persist(school);

                // Создаем объект Statistics
                StatisticsData statistics = new StatisticsData();
                statistics.setSchool(school);
                statistics.setStudents(Integer.parseInt(line[5]));
                statistics.setTeachers(Double.parseDouble(line[6]));
                statistics.setCalworks(Double.parseDouble(line[7]));
                statistics.setLunch(Double.parseDouble(line[8]));
                statistics.setComputer(Integer.parseInt(line[9]));
                statistics.setExpenditure(Double.parseDouble(line[10]));
                statistics.setIncome(Double.parseDouble(line[11]));
                statistics.setEnglish(Double.parseDouble(line[12]));
                statistics.setRead(Double.parseDouble(line[13]));
                statistics.setMath(Double.parseDouble(line[14]));

                em.persist(statistics);
            }

            transaction.commit();
            System.out.println("Данные перезаписаны.");
        } catch (IOException | NumberFormatException | CsvValidationException e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
