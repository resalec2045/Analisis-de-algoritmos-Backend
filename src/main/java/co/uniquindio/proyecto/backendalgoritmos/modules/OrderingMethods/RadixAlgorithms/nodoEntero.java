package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.RadixAlgorithms;

public class nodoEntero
{
    nodoEntero(int a)
    {

        valor = a;
        siguiente = null;
    }
    nodoEntero()
    {
        siguiente = null;
        valor = 0;
    }
    public int valor;
    public nodoEntero siguiente;
}
