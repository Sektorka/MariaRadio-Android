package eu.gyurasz.mariaradio;

import java.util.List;

public interface ILoaded extends IException {
    public <T> void Loaded(List<T> dataList);
}
