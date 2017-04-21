package digrafo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Digrafo {
	//HashMap contendo todos nodos do dígrafo
	private HashMap<Integer, Node> nodos = new HashMap<Integer, Node>();
	
	//Atributos usadas no método temCiclo
	public int t = 1;
	ArrayList<List<Node>> componentes = new ArrayList<List<Node>>();
	ArrayList<Node> componente;

	//Subclasse Nodo
	private class Node {
		
		private int nome;
		//Atributo para marcação no método semCiclo
		private int marcado = 0;
		//Atributo para marcação no método temCiclo
		private int marcadoC = 0;
		//ArrayList contendo referências para os vizinhos do Nodo
		private ArrayList<Node> vizinhos = new ArrayList<Node>();
	}

	
	//Método usado para ler o arquivo
	public void leArquivo(String path) throws IOException {
		//Armazena o arquivo no variável "reader"
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		
		//Variável onde ficará armazenadas cada linha
		String s;
		
		//Pula a linha "("
		reader.readLine();
		//Pula a linha com o número de nodos no grafo (Irrelevante para o código)
		reader.readLine();

		//Armazena a terceira linha, primeiro nodo, na variável "s"
		s = reader.readLine();

		
		//While usado para criar todos nodos, ele para quando a linha atribuida à "s" for a última linha: ")".
		while (s.startsWith("(")) {
			//Divide a linha em um array, carácter definado para fazer a quebra da linha foi espaço(" ")
			String[] c = s.split(" ");
			//Remove o ")" da última posição do array "c"  
			c[c.length - 1] = c[c.length - 1].replace(")", "");
			//Remove o "(" da primeira posição do array "c"  
			c[0] = c[0].replace("(", "");

			//Instancia um nodo n
			Node n = new Node();
			//Atribui ao atribuno "nome" de "n" o valor da primeira posição do array "c" (transformando de string para int)
			n.nome = Integer.parseInt(c[0]);
			//-->Insere o nodo "n" (OBJEOT0)no hashmap, usando o seu atributo "nome" como chave
			nodos.put(n.nome, n);//-> sempre faço só do primiero e vou para a proxima linha
			//"s" recebe a próxima linha
			s = reader.readLine();
		}
		//Armazena novamente o arquivo dentro de "reader", voltando para o inicio do arquivo
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				new File(path))));
		//Pula a linha "("
		reader.readLine();
		//Pula a linha com o número de nodos no grafo (Irrelevante para o código)
		reader.readLine();
		
		//Armazena a terceira linha, primeiro nodo, na variável "s"
		s = reader.readLine();

		while (s.startsWith("(")) {
			//Divide a linha em um array, carácter definado para fazer a quebra da linha foi espaço(" ")
			String[] c = s.split(" ");
			//Remove o ")" da última posição do array "c"  
			c[c.length - 1] = c[c.length - 1].replace(")", "");
			//Remove o "(" da primeira posição do array "c"  
			c[0] = c[0].replace("(", "");
			
			//Atribui ao nodo "v" (recém criado) uma referência ao nodo no hashmap com chave igual ao primeiro valor do array "c"
			//Dessa forma se o array for composto por {0,1,2} o nodo "v" recebe referência ao nodo com atributo nome igual a "0"
			Node v = nodos.get(Integer.parseInt(c[0]));//->digo a chave e ele me devolve o objeto(GET)
			
			//Insere REFERÊNCIAS dos vizinhos de "v" dentro do atributo arraList "vizinhos" de "v"
			for (int i = 1; i < c.length; i++) {
				//Insere a referência do nodo cujo a chave no HashMap seja o valor da posição "i" no verto "c"
				v.vizinhos.add(nodos.get(Integer.parseInt(c[i])));
			}
			//"s" recebe a próxima linha
			s = reader.readLine();
		}
		//Fecha o  BufferedReader
		reader.close();
	}
	
	//Método a ser chamado no main()
	private void run(String path) throws IOException {
		//Exectua o método de criar os nodos
		leArquivo(path);
		//Cria um array de nodos cujo o tamanho é igual ao tamanho do hashmap
		Node[] ns = new Node[nodos.size()];
		//Cria a variável count
		int count = 0;
		
		//"For each" em que é atribuido à variável "key" o valor de cada chave do HashMap
		for (int key : nodos.keySet()) { //->key set devolve um array de chaves
			//Insere no array "ns", na posição "count" o nodo cuja a chave no hashmap é igual a chave atribuida à variável "key"
			ns[count] = nodos.get(key);// -> aqui pego o objeto numa lista pq hash map é + fácil de guardar, mas o array mais facil de manipular
			//Incrementa o "count"
			count++;
			// System.out.println(nodos.get(key).nome);
		}
		//Executa o método semCiclo passando como parâmetro o array "ns"
		semCiclo(ns);
	}
	
	//Os métodos semCiclo assumem que não há ciclos dentro do dígrafo então geram a ordem topológica do dígrafo.
	//Caso um ciclo seja encontrado será inciado o método temCiclo e a ordem topológica gerada não é imprimida
	private void semCiclo(Node[] ns) {//->PROVA POR CONTRADIÇÃO**
		//Variável s onde será inserida a ordem topológica
		String s = "";// -->começava nula, null pointer exception, dai fiz assim
		//Variáveis para verificação do While
		int i = 0;
		boolean parar = false;
		String t = s;
		//While para executar o método semCiclo para cada Nodo que ainda não fora marcado
		while(i < ns.length && !parar){
			if (ns[i].marcado == 0) {
				//Executa o método semCiclo passando como parâmetro "s" e o nodo na posição "i" do vetor "ns";
				s = semCiclo(ns[i], t)+s;
				//Se "s" contém um ) significa que um ciclo fora encontrado então dá o valor "true" à "parar" para terminar o while
				if (s.contains(")")){
					
					parar = true;
				}
				
			}
			
			i++;
		}
		
		
		//Se "s" contém um ) significa que um ciclo fora encontrado então executa o método temCiclo passando o vetor "ns" como parâmetro
		if (s.contains(")")) {
			temCiclo(ns);
			//Executa o return para sair do método semCiclo
			return;
		}
		//Caso chegue aqui não foi encontrado ciclos então é imprimida a ordem topolígica do grafo
		System.out.println("Ordem topológica: " + s);
	}

	//Método semCiclo recursivo
	private String semCiclo(Node n, String s) {
		//Marca o nodo com o valor "1"
		n.marcado = 1;
		//Cria uma variável t, cópia de s.
		String t = s;
		//"For each" que atribui ao nodo "v" a referência de cada nodo no ArrayList "vizinhos" de "n"
		for (Node v : n.vizinhos) {//->PROFUNDIDADE
			//Se "v" está marcado com o valor "1" significa que um ciclo fora encontrado então se retorna um ")" para se identificar no primeiro método semCiclo que há um ciclo no grafo
			if (v.marcado == 1) {
				return ")";
			}
			// Se "v" está marcado como "0", executa o método semCiclo passando a string "t" e o nodo "v" como parâmetros
			if (v.marcado == 0) {
				//Concatena a frente de "s" o valor recebido pela recursão
				//No caso se: s == "92" e a recursão retornar "43", "s" passa a ser "4392" 
				s = semCiclo(v, t) + s;
			}
		}
		//Marca o nodo com o valor "2"
		n.marcado = 2;
		//Retorna "s" com o nome do nodo concatenado a sua frente
		return s = n.nome + s;
	}

	//Métodos temCiclo para a idenficação dos componentes do dígrafo com ciclo 
	private void temCiclo(Node[] ns) {
		//"For each" que atribui ao nodo "n" referências para cada nodo no array "ns" que ainda não fora marcado
		for (Node n : ns) {
			if (n.marcadoC == 0) {
				//Cria um componente vazio (ArrayList de nodos)
				componente = new ArrayList<Node>();
				//Verifica se "n" possui algum vizinho
				if (n.vizinhos.isEmpty()) {
					//Coloca "n" no componente
					componente.add(n);
					//Coloca o componente no ArrayList de componentes "componentes"
					componentes.add(componente);
				} else  {
					//Marca "n" como o incremento de "t"
					n.marcadoC = t++;
					//Executa o método temCiclo passando como parâmetros o nodo "n" e o valor de seu atributo marcadoC
					temCiclo(n, n.marcadoC);
					//Coloca o componente no ArrayList de componentes "componentes"
					componentes.add(componente);//->BOTO O COMPONENTE NA LISTA DE COMPONENTES
				}
			}
			

		}
		//Imprime os Componentes
		for (List<Node> l : componentes) {
			System.out.print("[");
			for (Node o : l) {
				System.out.print(o.nome+" ");
			}
			System.out.println("]");
		}
	}
	//Método temCiclo recursivo
	public boolean temCiclo(Node n, int t) {
		//Cria a variável "bool" com o valor "false" usado para verificar se o nodo faz parte do componente
 		boolean bool = false;
		//"For each" que atribui ao nodo "v" a referência de cada nodo no ArrayList "vizinhos" de "n"
		for (Node v : n.vizinhos) {
			//Verifica se "v" está marcado com o valor "0"
			if (v.marcadoC == 0) {
				//Marca com o valor "-1" (Para evitar loop infinito)
				v.marcadoC = -1;
				//Executa o método temCiclo passando o nodo "v" e o valor "t" como parâmetros e verifica o boolean retornado pela recursão
				if (temCiclo(v, t)) {
					//Marca "n" com o valor "t"
					n.marcadoC = t;
					//Marca bool como "true"
					bool = true;
				}
				//Se "v" ainda está marcado como "-1" ele volta a ser marcado com "0"
				if(v.marcadoC == -1){
					v.marcadoC = 0;
				}
			}
			//Se "v" está marcadoc como "t", marca "n" como "t" e atribui a "bool" o valor "true"
			else if (v.marcadoC == t) {
				n.marcadoC = t;
				bool = true;
			}

		}
		//Se "n" está marcado como "t", adiciona ele ao componente
		if (n.marcadoC == t) {
			componente.add(n);
		}
		
		//Retorna o valor de "bool" para identificar se "n" faz parte do componente
		return bool;
	}
	
	//Método Main
	public static void main(String[] args) throws IOException {
		//Instancia um Digrafo "d"
		Digrafo d = new Digrafo();
		//Digrafo "d" executa o método run passando o caminho para o arquivo contendo as informações do grafo
		d.run("C://Users//Mario//Desktop//teste.txt");
	}
}
