import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
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

        final int kuplaSize = 7; //k
        final int bitSetSize = 128; //m
        final int bitsPorElemento = 10;//b;
        final int maxNumKuplas = 1200; //n;
        boolean foo = true;
        long inicioDoPre;
        long fimDoPre;
        long acumulado = 0L;
        String alvo = "Sherlock";
        List<BloomFilter> umaPraCadaLinha = new ArrayList();
//        boolean primeirapalavra = true;


        File entrada = new File("big.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(entrada)))
        {
            String line;
            while ((line = br.readLine()) != null) //pega as linhas
            {
                // como tem muito IO, a contagem de tempo não é tão simples

                inicioDoPre = System.currentTimeMillis();

                BloomFilter<String> naoEhMesmo = new BloomFilter(bitSetSize, bitsPorElemento, maxNumKuplas, foo); // um filter por linha
                //BloomFilter<String> naoEhMesmo = new BloomFilter(10240,120); // um filter por linha
                String[] palavrasDaLinha = line.split(" "); // divide a linha em palavras
                for(String palavra : palavrasDaLinha)
                {
//                    if (primeirapalavra)
//                    {
//                        alvo = palavra;
//                        primeirapalavra = false;
//                    }

                    for (int indexDeKupla = 0;(palavra.length() - indexDeKupla) >= kuplaSize; indexDeKupla++) // divide a linha em kuplas
                    {
                        char[] kupla = new char[kuplaSize] ;
                        palavra.getChars(indexDeKupla, indexDeKupla+kuplaSize, kupla, 0);// bota kuplaSize chars em temp

                        naoEhMesmo.add(String.valueOf(kupla));
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

        boolean[] oQCadaLinhaRetorna = new boolean[umaPraCadaLinha.size()];

        inicioDoPre = System.currentTimeMillis();

        List<String> kuplaAlvo = new ArrayList(); // kuplas do alvo

        for (int indexDeKupla = 0;(alvo.length() - indexDeKupla) >= kuplaSize; indexDeKupla++) // Constroi as kuplas do alvo
        {
            char[] kupla = new char[kuplaSize] ;
            alvo.getChars(indexDeKupla, indexDeKupla+kuplaSize, kupla, 0);
            kuplaAlvo.add(String.valueOf(kupla));
        }

        int index = 0;
        for (BloomFilter linha : umaPraCadaLinha) // testa linha a linha
        {
            boolean matchPerfeito = true;
            for( String kAlvo : kuplaAlvo) // pra cada kupla do alvo
            {
                if(!linha.contains(kAlvo)) //se pelo menos uma delas não bater
                {
                    matchPerfeito = false; // então não tem o alvo
                }
            }
            oQCadaLinhaRetorna[index] = matchPerfeito;
            index++;
        }

        fimDoPre = System.currentTimeMillis();

        System.out.print("Tempo consultando: " + (fimDoPre - inicioDoPre) + "\n");

        int totalTrue =0;
        for (boolean retorno : oQCadaLinhaRetorna)
        {
//            System.out.print(retorno + "\n");
            if(retorno)
                totalTrue++;
        }

        System.out.print("Total de trues: " + totalTrue + "\n");

        System.out.print("Alvo: "+ alvo + "\n");

//        int maxKuplas = 0;
//        for (BloomFilter linha : umaPraCadaLinha)
//        {
//            maxKuplas = Math.max(maxKuplas, linha.getNumberOfAddedElements());
//        }
//
//        System.out.print(maxKuplas + "\n");
    }
}