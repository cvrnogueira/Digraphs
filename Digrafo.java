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
	//HashMap contendo todos nodos do d�grafo
	private HashMap<Integer, Node> nodos = new HashMap<Integer, Node>();
	
	//Atributos usadas no m�todo temCiclo
	public int t = 1;
	ArrayList<List<Node>> componentes = new ArrayList<List<Node>>();
	ArrayList<Node> componente;

	//Subclasse Nodo
	private class Node {
		
		private int nome;
		//Atributo para marca��o no m�todo semCiclo
		private int marcado = 0;
		//Atributo para marca��o no m�todo temCiclo
		private int marcadoC = 0;
		//ArrayList contendo refer�ncias para os vizinhos do Nodo
		private ArrayList<Node> vizinhos = new ArrayList<Node>();
	}

	
	//M�todo usado para ler o arquivo
	public void leArquivo(String path) throws IOException {
		//Armazena o arquivo no vari�vel "reader"
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		
		//Vari�vel onde ficar� armazenadas cada linha
		String s;
		
		//Pula a linha "("
		reader.readLine();
		//Pula a linha com o n�mero de nodos no grafo (Irrelevante para o c�digo)
		reader.readLine();

		//Armazena a terceira linha, primeiro nodo, na vari�vel "s"
		s = reader.readLine();

		
		//While usado para criar todos nodos, ele para quando a linha atribuida � "s" for a �ltima linha: ")".
		while (s.startsWith("(")) {
			//Divide a linha em um array, car�cter definado para fazer a quebra da linha foi espa�o(" ")
			String[] c = s.split(" ");
			//Remove o ")" da �ltima posi��o do array "c"  
			c[c.length - 1] = c[c.length - 1].replace(")", "");
			//Remove o "(" da primeira posi��o do array "c"  
			c[0] = c[0].replace("(", "");

			//Instancia um nodo n
			Node n = new Node();
			//Atribui ao atribuno "nome" de "n" o valor da primeira posi��o do array "c" (transformando de string para int)
			n.nome = Integer.parseInt(c[0]);
			//-->Insere o nodo "n" (OBJEOT0)no hashmap, usando o seu atributo "nome" como chave
			nodos.put(n.nome, n);//-> sempre fa�o s� do primiero e vou para a proxima linha
			//"s" recebe a pr�xima linha
			s = reader.readLine();
		}
		//Armazena novamente o arquivo dentro de "reader", voltando para o inicio do arquivo
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				new File(path))));
		//Pula a linha "("
		reader.readLine();
		//Pula a linha com o n�mero de nodos no grafo (Irrelevante para o c�digo)
		reader.readLine();
		
		//Armazena a terceira linha, primeiro nodo, na vari�vel "s"
		s = reader.readLine();

		while (s.startsWith("(")) {
			//Divide a linha em um array, car�cter definado para fazer a quebra da linha foi espa�o(" ")
			String[] c = s.split(" ");
			//Remove o ")" da �ltima posi��o do array "c"  
			c[c.length - 1] = c[c.length - 1].replace(")", "");
			//Remove o "(" da primeira posi��o do array "c"  
			c[0] = c[0].replace("(", "");
			
			//Atribui ao nodo "v" (rec�m criado) uma refer�ncia ao nodo no hashmap com chave igual ao primeiro valor do array "c"
			//Dessa forma se o array for composto por {0,1,2} o nodo "v" recebe refer�ncia ao nodo com atributo nome igual a "0"
			Node v = nodos.get(Integer.parseInt(c[0]));//->digo a chave e ele me devolve o objeto(GET)
			
			//Insere REFER�NCIAS dos vizinhos de "v" dentro do atributo arraList "vizinhos" de "v"
			for (int i = 1; i < c.length; i++) {
				//Insere a refer�ncia do nodo cujo a chave no HashMap seja o valor da posi��o "i" no verto "c"
				v.vizinhos.add(nodos.get(Integer.parseInt(c[i])));
			}
			//"s" recebe a pr�xima linha
			s = reader.readLine();
		}
		//Fecha o  BufferedReader
		reader.close();
	}
	
	//M�todo a ser chamado no main()
	private void run(String path) throws IOException {
		//Exectua o m�todo de criar os nodos
		leArquivo(path);
		//Cria um array de nodos cujo o tamanho � igual ao tamanho do hashmap
		Node[] ns = new Node[nodos.size()];
		//Cria a vari�vel count
		int count = 0;
		
		//"For each" em que � atribuido � vari�vel "key" o valor de cada chave do HashMap
		for (int key : nodos.keySet()) { //->key set devolve um array de chaves
			//Insere no array "ns", na posi��o "count" o nodo cuja a chave no hashmap � igual a chave atribuida � vari�vel "key"
			ns[count] = nodos.get(key);// -> aqui pego o objeto numa lista pq hash map � + f�cil de guardar, mas o array mais facil de manipular
			//Incrementa o "count"
			count++;
			// System.out.println(nodos.get(key).nome);
		}
		//Executa o m�todo semCiclo passando como par�metro o array "ns"
		semCiclo(ns);
	}
	
	//Os m�todos semCiclo assumem que n�o h� ciclos dentro do d�grafo ent�o geram a ordem topol�gica do d�grafo.
	//Caso um ciclo seja encontrado ser� inciado o m�todo temCiclo e a ordem topol�gica gerada n�o � imprimida
	private void semCiclo(Node[] ns) {//->PROVA POR CONTRADI��O**
		//Vari�vel s onde ser� inserida a ordem topol�gica
		String s = "";// -->come�ava nula, null pointer exception, dai fiz assim
		//Vari�veis para verifica��o do While
		int i = 0;
		boolean parar = false;
		String t = s;
		//While para executar o m�todo semCiclo para cada Nodo que ainda n�o fora marcado
		while(i < ns.length && !parar){
			if (ns[i].marcado == 0) {
				//Executa o m�todo semCiclo passando como par�metro "s" e o nodo na posi��o "i" do vetor "ns";
				s = semCiclo(ns[i], t)+s;
				//Se "s" cont�m um ) significa que um ciclo fora encontrado ent�o d� o valor "true" � "parar" para terminar o while
				if (s.contains(")")){
					
					parar = true;
				}
				
			}
			
			i++;
		}
		
		
		//Se "s" cont�m um ) significa que um ciclo fora encontrado ent�o executa o m�todo temCiclo passando o vetor "ns" como par�metro
		if (s.contains(")")) {
			temCiclo(ns);
			//Executa o return para sair do m�todo semCiclo
			return;
		}
		//Caso chegue aqui n�o foi encontrado ciclos ent�o � imprimida a ordem topol�gica do grafo
		System.out.println("Ordem topol�gica: " + s);
	}

	//M�todo semCiclo recursivo
	private String semCiclo(Node n, String s) {
		//Marca o nodo com o valor "1"
		n.marcado = 1;
		//Cria uma vari�vel t, c�pia de s.
		String t = s;
		//"For each" que atribui ao nodo "v" a refer�ncia de cada nodo no ArrayList "vizinhos" de "n"
		for (Node v : n.vizinhos) {//->PROFUNDIDADE
			//Se "v" est� marcado com o valor "1" significa que um ciclo fora encontrado ent�o se retorna um ")" para se identificar no primeiro m�todo semCiclo que h� um ciclo no grafo
			if (v.marcado == 1) {
				return ")";
			}
			// Se "v" est� marcado como "0", executa o m�todo semCiclo passando a string "t" e o nodo "v" como par�metros
			if (v.marcado == 0) {
				//Concatena a frente de "s" o valor recebido pela recurs�o
				//No caso se: s == "92" e a recurs�o retornar "43", "s" passa a ser "4392" 
				s = semCiclo(v, t) + s;
			}
		}
		//Marca o nodo com o valor "2"
		n.marcado = 2;
		//Retorna "s" com o nome do nodo concatenado a sua frente
		return s = n.nome + s;
	}

	//M�todos temCiclo para a idenfica��o dos componentes do d�grafo com ciclo 
	private void temCiclo(Node[] ns) {
		//"For each" que atribui ao nodo "n" refer�ncias para cada nodo no array "ns" que ainda n�o fora marcado
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
					//Executa o m�todo temCiclo passando como par�metros o nodo "n" e o valor de seu atributo marcadoC
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
	//M�todo temCiclo recursivo
	public boolean temCiclo(Node n, int t) {
		//Cria a vari�vel "bool" com o valor "false" usado para verificar se o nodo faz parte do componente
 		boolean bool = false;
		//"For each" que atribui ao nodo "v" a refer�ncia de cada nodo no ArrayList "vizinhos" de "n"
		for (Node v : n.vizinhos) {
			//Verifica se "v" est� marcado com o valor "0"
			if (v.marcadoC == 0) {
				//Marca com o valor "-1" (Para evitar loop infinito)
				v.marcadoC = -1;
				//Executa o m�todo temCiclo passando o nodo "v" e o valor "t" como par�metros e verifica o boolean retornado pela recurs�o
				if (temCiclo(v, t)) {
					//Marca "n" com o valor "t"
					n.marcadoC = t;
					//Marca bool como "true"
					bool = true;
				}
				//Se "v" ainda est� marcado como "-1" ele volta a ser marcado com "0"
				if(v.marcadoC == -1){
					v.marcadoC = 0;
				}
			}
			//Se "v" est� marcadoc como "t", marca "n" como "t" e atribui a "bool" o valor "true"
			else if (v.marcadoC == t) {
				n.marcadoC = t;
				bool = true;
			}

		}
		//Se "n" est� marcado como "t", adiciona ele ao componente
		if (n.marcadoC == t) {
			componente.add(n);
		}
		
		//Retorna o valor de "bool" para identificar se "n" faz parte do componente
		return bool;
	}
	
	//M�todo Main
	public static void main(String[] args) throws IOException {
		//Instancia um Digrafo "d"
		Digrafo d = new Digrafo();
		//Digrafo "d" executa o m�todo run passando o caminho para o arquivo contendo as informa��es do grafo
		d.run("C://Users//Mario//Desktop//teste.txt");
	}
}
