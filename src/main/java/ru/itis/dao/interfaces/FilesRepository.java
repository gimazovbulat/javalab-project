package ru.itis.dao.interfaces;

import ru.itis.models.FileInfo;

public interface FilesRepository extends CrudRepository<Long, FileInfo>{
    FileInfo findByName(String name);
}
