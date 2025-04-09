package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.RadixAlgorithms;

public class ColaEnlazada
{
    private nodoEntero inicio;
    private nodoEntero fin;
    private int tamano;
    public ColaEnlazada()
    {
        inicio = null;
        fin = null;
        tamano = 0;
    }
    public void encolar(int num)
    {
        tamano++;
        nodoEntero temp= new nodoEntero(num);
        if (inicio == null)
        {
            inicio = temp;
            fin = inicio;
        }
        else
        {
            fin.siguiente = temp;
            fin = temp;

        }
        temp = null;
    }
    public int decolar()
    {
        tamano--;
        int temp = inicio.valor;
        nodoEntero nodoTemp;
        nodoTemp = inicio;
        inicio = inicio.siguiente;
        nodoTemp = null;
        return temp;
    }
    public boolean estaVacia()
    {
        return (tamano == 0);
    }
}