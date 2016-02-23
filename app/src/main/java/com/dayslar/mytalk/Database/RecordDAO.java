package com.dayslar.mytalk.Database;

import com.dayslar.mytalk.Models.Manager;
import com.dayslar.mytalk.Models.Record;
import com.dayslar.mytalk.Models.SharedRecord;

import java.util.List;

public interface RecordDAO {

    //Добавляет нового менеджера
    void addManager(Manager manager);

    //Обновляет информацию о менеджеры с указаным ид
    void updateManager(int id, Manager manager);

    //удаляет менеджера по ид
    void deleteManager(int id);

    //возвращает список менеджеров
    List<Manager> getManagers();

    //добавляет новую запись в базу
    void addRecord(Record record);

    //добавляет новую запись в базу
    void addSharedRecord(SharedRecord sharedRecord);

    //возвращает запись по ид
    Record getRecord(int id);

    //удаляет запись по ид
    void deleteRecord(int id);

    //возвращает список записей
    List<Record> getRecords();
}
