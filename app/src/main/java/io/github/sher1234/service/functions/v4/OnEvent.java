package io.github.sher1234.service.functions.v4;

public interface OnEvent<Data> {
    void onPostExecute(Data data);
    void onPreExecute();
}