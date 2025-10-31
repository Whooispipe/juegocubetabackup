package puppy.code;

public interface IGotaReceptor {
    void recibirDano(int cantidad);
    void curar(int cantidad);
    void aplicarBoostVelocidad(float porcentaje, float duracion);
    void sumarPuntos(int puntos);
    boolean estaMuerto();
}