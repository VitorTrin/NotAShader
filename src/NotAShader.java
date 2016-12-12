import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitor on 12/12/16.
 */
public class NotAShader // podia não existir e ser um bloomfilter direto, mas facilita a construção
{


//    public BloomFilter bloomFilter;
//
//
//
//    public NotAShader(int bitSetSize, int bitsPorElemento, int maxNumKuplas)
//    {
//        this.bloomFilter= new BloomFilter<String>(bitSetSize, bitsPorElemento, maxNumKuplas, null);
//    }
//
//    public BloomFilter getBloom(){
//        return this.bloomFilter;
//    }



    public static void main(String args[]) {

        final int kuplaSize = 1; //k
        final int bitSetSize = 500; //m
        final int bitsPorElemento = 1;//b;
        final int maxNumKuplas = 2000; //n;
        boolean foo = true;
        long inicioDoPre;
        long fimDoPre;
        long acumulado = 0L;

        List<BloomFilter> umaPraCadaLinha = new ArrayList();


        File entrada = new File("entrada.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(entrada)))
        {
            String line;
            while ((line = br.readLine()) != null) //pega as linhas
            {
                // como tem muito IO, a contagem de tempo não é tão simples

                inicioDoPre = System.currentTimeMillis();

                BloomFilter<String> naoEhMesmo = new BloomFilter(bitSetSize, bitsPorElemento, maxNumKuplas, foo); // um filter por linha
                String[] palavrasDaLinha = line.split(" "); // divide a linha em palavras
                for(String palavra : palavrasDaLinha)
                {
                    for (int indexDeKupla = 0;(palavra.length() - indexDeKupla) >= kuplaSize; indexDeKupla++) // divide a linha em kuplas
                    {
                        char[] kupla = new char[kuplaSize] ;
                        palavra.getChars(indexDeKupla, indexDeKupla+kuplaSize, kupla, 0);// bota kuplaSize chars em temp

                        naoEhMesmo.add(kupla.toString());
                    }
                }
                umaPraCadaLinha.add(naoEhMesmo);

                fimDoPre = System.currentTimeMillis();

                acumulado += fimDoPre - inicioDoPre;

            }
        }
        catch (Exception e){
            System.out.print("Não achou o arquivo");
        }

        System.out.print("Tempo usado preprocessando: " + acumulado + "\n");

        List<Boolean> oQCadaLinhaRetorna = new ArrayList();

        inicioDoPre = System.currentTimeMillis();

        for (BloomFilter linha : umaPraCadaLinha)
        {
            oQCadaLinhaRetorna.add(linha.contains("xx"));
        }

        fimDoPre = System.currentTimeMillis();

        System.out.print("Tempo consultando: " + (fimDoPre - inicioDoPre) + "\n");

        for (Boolean retorno : oQCadaLinhaRetorna)
        {
            System.out.print(retorno + "\n");
        }
    }
}