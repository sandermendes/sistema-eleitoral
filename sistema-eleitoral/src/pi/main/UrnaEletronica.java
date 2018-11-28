package pi.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import pi.DAO.DAOCandidato;
import pi.DAO.DAOEleitor;
import pi.arvore.ArvoreAVL;
import pi.arvore.ArvoreBinariaDeBusca;
import pi.model.Candidato;
import pi.model.Eleitor;
import pi.model.Partido;
import pi.model.TipoCandidato;
import pi.model.UF;

public class UrnaEletronica {
	private static ArvoreBinariaDeBusca<Eleitor> abEleitor = null;
	private static ArvoreBinariaDeBusca<Candidato> abCandidato = null;
	private static List<Integer> codigoMunicipioTO = new ArrayList<Integer>();
	private static List<Integer> codigoMunicipioPA = new ArrayList<Integer>();
	private static List<Integer> codigoMunicipioMA = new ArrayList<Integer>();
	private static Scanner sc = new Scanner(System.in);
	private static Random rand = new Random();

	private static void carregaDados() {
		abCandidato = DAOCandidato.lerArquivos();
		abEleitor = DAOEleitor.lerArquivo(abCandidato, 1);
		DAOEleitor.lerArquivos("eleitores//codigoMunicipioTO", codigoMunicipioTO);
		DAOEleitor.lerArquivos("eleitores//codigoMunicipioPA", codigoMunicipioPA);
		DAOEleitor.lerArquivos("eleitores//codigoMunicipioMA", codigoMunicipioMA);
	}

	public static void menu() {
		carregaDados();

		int opcao = 0;
		do {
			System.out.println("\nDigite a op��o desejada:");
			System.out.println("1- Adicionar novo Candidato");
			System.out.println("2- Votar");
			System.out.println("3- Mostrar Candidatos");
			System.out.println("4- Buscar por eleitor");
			System.out.println("5- Comparar Arvores");
			System.out.println("0- Encerrar Elei��o");
			opcao = Integer.parseInt(sc.nextLine());
			switch (opcao) {
			case 1:
				adicionaCandidato();
				break;
			case 2:
				votar();
				break;

			case 3:
				mostrarCandidatos();
				break;
			case 4:
				buscarEleitor();
				break;
			case 5:
				comparaArvores();
				break;

			}
		} while (opcao != 0);
		System.out.println("Encerrando Elei��o");
		mostraVencedor();
	}

	private static void buscarEleitor() {
		System.out.println("Digite o CPF do eleitor");
		long cpf = Long.parseLong(sc.nextLine());
		Eleitor eleitor = abEleitor.buscaBinaria(cpf);
		if (eleitor != null) {
			System.out.println(
					String.format("CPF: %d\nCandidato federal em quem votou: %s\nCandidato Regional em quem votou: %s",
							eleitor.getElemento(), eleitor.getCandidatoFederal().getNome(),
							eleitor.getCandidatoRegional().getNome()));
		} else
			System.out.println("Eleitor n�o encontrado");
	}

	private static void votar() {
		UF uf = null;
		int codigoMunicipio;
		boolean contains = false;
		System.out.println("Esolha a UF: ");
		Arrays.asList(UF.values()).forEach(System.out::println);
		do {
			uf = UF.getUF(Short.parseShort(sc.nextLine()));
			if (uf == null)
				System.out.println("UF inv�lida, tente novamente");
		} while (uf == null);
		do {
			System.out.println("Deseja visualisar o codigo de municipios?\n1 - Sim\n2 - N�o");
			int opcao = Integer.parseInt(sc.nextLine());
			do {
				switch (opcao) {
				case 1:
					mostraCodigos(uf);
					break;
				case 2:
					break;
				default:
					System.out.println("Op��o inv�lida");
				}
			} while (opcao != 1 && opcao != 2);
			System.out.println("Digite o codigo de municipio: ");
			codigoMunicipio = Integer.parseInt(sc.nextLine());
			if (uf.equals(UF.MA))
				contains = codigoMunicipioMA.contains(codigoMunicipio);
			else if (uf.equals(UF.PA))
				contains = codigoMunicipioPA.contains(codigoMunicipio);
			else
				contains = codigoMunicipioTO.contains(codigoMunicipio);
			if (!contains)
				System.out.println("Codigo de Municipio inv�lido, tente novamente");
		} while (!contains);

		boolean candCerto = false;
		Candidato candReg = null;
		Candidato candFed = null;
		do {
			System.out.println("Deseja listar os candidatos regionais?\n1 - sim\n2- n�o");
			listaCandidatos(TipoCandidato.REGIONAL);

			System.out.println("Digite o codigo do Candidato Regional");
			long codCandReg = Long.parseLong(sc.nextLine());
			candReg = abCandidato.buscaBinaria(codCandReg);
			if (candReg != null && candReg.getTipoCandidato() == TipoCandidato.REGIONAL)
				candCerto = true;
			if (candCerto == false)
				System.out.println("Candidato inv�lido, tente novamente");
		} while (!candCerto);
		candCerto = false;
		do {
			System.out.println("Deseja listar os Candidatos Federais?\n1 - Sim\n2 - N�o");
			listaCandidatos(TipoCandidato.FEDERAL);

			System.out.println("Digite o codigo do candidato Federal");
			long codCandFed = Long.parseLong(sc.nextLine());
			candFed = abCandidato.buscaBinaria(codCandFed);
			if (candFed != null && candFed.getTipoCandidato() == TipoCandidato.FEDERAL)
				candCerto = true;
			if (candCerto == false)
				System.out.println("Candidato inv�lido, tente novamente");
		} while (!candCerto);
		System.out.println("Digite seu CPF");
		boolean add = false;
		do {
			long cpf = Long.parseLong(sc.nextLine());
			Eleitor busca = abEleitor.buscaBinaria(cpf);
			if (busca == null) {
				Eleitor eleitor = new Eleitor(uf, cpf, codigoMunicipio, candFed, candReg);
				add = abEleitor.add(eleitor, true);
				System.out.println("Eleitor adicionado com sucesso");
			} else
				System.out.println("Erro ao realizar o voto, CPF repetido, tente novamente");

		} while (!add);
	}

	private static void listaCandidatos(TipoCandidato tipo) {
		int opcao = Integer.parseInt(sc.nextLine());
		do {
			switch (opcao) {
			case 1:
				abCandidato.toList().stream().filter(c -> c.getTipoCandidato() == tipo)
						.forEach(c -> System.out.println(String.format("Nome: %s\nPartido: %s\nCodigo Candidato: %d\n",
								c.getNome(), c.getPartido(), c.getElemento())));
				break;
			case 2:
				break;
			default:
				System.out.println("Op��o inv�lida");
				break;
			}
		} while (opcao != 1 && opcao != 2);
	}

	private static void mostraCodigos(UF uf) {
		if (uf.equals(UF.MA)) {
			percorreCodigos(codigoMunicipioMA);
		} else if (uf.equals(UF.PA))
			percorreCodigos(codigoMunicipioPA);
		else
			percorreCodigos(codigoMunicipioTO);
	}

	private static void percorreCodigos(List<Integer> codigo) {

		for (int x = 0; x < codigo.size(); x++) {
			System.out.print(codigo.get(x) + "\t");
			if (x % 6 == 0)
				System.out.println();
		}
	}

	private static void mostrarCandidatos() {
		List<Candidato> list = abCandidato.toList();
		list.forEach(c -> {
			System.out.println(String.format("Nome: %s\tQuantidade de votos: %d\tPartido: %s\tTipo Candidato: %s",
					c.getNome(), c.getQtdVotos(), c.getPartido(), c.getTipoCandidato()));
		});
	}

	private static void adicionarEleitor() {
		Arrays.asList(abCandidato.toList().stream().toArray()).stream()
				.filter(c -> ((Candidato) c).getTipoCandidato() == TipoCandidato.FEDERAL);

		Arrays.asList(abCandidato.toList().stream().toArray()).stream()
				.filter(c -> ((Candidato) c).getTipoCandidato() == TipoCandidato.REGIONAL);
	}

	private static void adicionaCandidato() {
		TipoCandidato tipoCandidato = TipoCandidato.REGIONAL;

		System.out.println("Digite o nome do candidato: ");
		String nome = sc.nextLine();

		mostraPartidos();
		Partido partido = escolhePartido();

		Candidato cand = new Candidato(nome, partido, tipoCandidato);

		if (abCandidato.add(cand, true))
			System.out.println("Candidato inserido com sucesso");
		else
			System.out.println("\n\nCodigo do candidato " + cand.getElemento());

	}

	private static Partido escolhePartido() {
		Partido partido = null;
		do {
			partido = Partido.getPartido(Integer.parseInt(sc.nextLine()));
			if (partido == null)
				System.out.println("Partido inv�lido, tente novamente");
		} while (partido == null);
		return partido;
	}

	private static void mostraPartidos() {
		System.out.println("Digite o Partido desejado");
		Arrays.asList(Partido.values()).forEach(System.out::println);
	}

	private static void comparaArvores() {
		Random rand = new Random();
		ArvoreAVL<Eleitor> abAVL = new ArvoreAVL<Eleitor>();
		abEleitor.toList().forEach(abAVL::add);
		List<Object> listRegional = Arrays.asList(
				(abCandidato.toList().stream().filter(c -> c.getTipoCandidato() == TipoCandidato.REGIONAL).toArray()));
		List<Object> listFederal = Arrays.asList(
				(abCandidato.toList().stream().filter(c -> c.getTipoCandidato() == TipoCandidato.FEDERAL).toArray()));
//		boolean grava = false;
		List<Eleitor> list = new ArrayList<Eleitor>();
		Candidato cand1 = new Candidato("4", Partido.PT, TipoCandidato.REGIONAL);
		Candidato cand2 = new Candidato("3", Partido.PT, TipoCandidato.FEDERAL);
		System.out.println("Digite a quantidade de eleitores que deseja gerar");
		long qtdAdd = Long.parseLong(sc.nextLine());
		System.out.println("Digite a quantidade de buscas que deseja realizar.. recomendado < 100");
		long qtdBusca = Long.parseLong(sc.nextLine());
//		System.out.println("Deseja que os eleitores gerados sejam guardados no arquivo?\n1 - Sim\n2 - N�o");
		int opcao = 0;

		for (int x = 0; x < qtdAdd; x++) {
			Candidato candRegional = ((Candidato) listRegional.get(rand.nextInt(listRegional.size())));
			Candidato candFederal = ((Candidato) listFederal.get(rand.nextInt(listFederal.size())));
			list.add(new Eleitor(UF.MA, rand.nextLong(), 0, candFederal, candRegional));
		}

		long iniRubro = System.currentTimeMillis();
		for (Eleitor eleitor : list) {
			abEleitor.add(eleitor);
		}
		long fimRubro = System.currentTimeMillis();
		long iniAVL = System.currentTimeMillis();

		for (Eleitor eleitor : list) {
			abAVL.add(eleitor);
		}
		long fimAVL = System.currentTimeMillis();
		System.out.println("\tArvore Rubro Negra");
		System.out.println(
				"Tempo que demorou para inserir na Rubro: " + (((double) fimRubro - iniRubro) / 1000) + " Segundos");
		System.out.println("RAIZ: " + abEleitor.raiz.getElemento());
		System.out.println("Tamanho da arvoreRubro: " + abEleitor.toList().size());
		System.out.println("-------");
		System.out.println("\tArvore AVL");
		System.out
				.println("Tempo que demorou para inserir na AVL: " + (((double) fimAVL - iniAVL) / 1000) + " Segundos");
		System.out.println("RAIZ: " + abAVL.raiz.getElemento());
		System.out.println("Tamanho da arvoreAVL: " + abAVL.toList().size());
		System.out.println("----------");
		System.out.println("\tBusca na Arvore Rubro Negra");
		List<Integer> listInt = new ArrayList<Integer>();
		for (int x = 0; x < qtdBusca; x++) {
			listInt.add(rand.nextInt());
		}

		long ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abEleitor.buscaBinaria(x);
		}
		long fim = System.currentTimeMillis();

		System.out.println("Busca Binaria na Rubro Negra: " + (((double) fim - ini) / 1000) + " Segundos");

		ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abEleitor.buscaProfundidade(x);
		}
		fim = System.currentTimeMillis();
		System.out.println("Busca Profundidade na Rubro Negra: " + (((double) fim - ini) / 1000) + " Segundos");

		ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abEleitor.buscaLargura(x);
		}
		fim = System.currentTimeMillis();
		System.out.println("Busca Largura na Rubro Negra: " + (((double) fim - ini) / 1000) + " Segundos");
		System.out.println("Quantidade de elementos pesquisados: " + qtdBusca);

		System.out.println("---------");
		System.out.println("\tBusca na �rvore AVl");
		ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abAVL.buscaBinaria(x);
		}
		fim = System.currentTimeMillis();

		System.out.println("Busca Binaria na AVL: " + (((double) fim - ini) / 1000) + " Segundos");

		ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abAVL.buscaProfundidade(x);
		}
		fim = System.currentTimeMillis();
		System.out.println("Busca Profundidade na AVL " + (((double) fim - ini) / 1000) + " Segundos");

		ini = System.currentTimeMillis();
		for (Integer x : listInt) {
			abAVL.buscaLargura(x);
		}
		fim = System.currentTimeMillis();
		System.out.println("Busca Largura na AVL: " + (((double) fim - ini) / 1000) + " Segundos");
		System.out.println("Quantidade de elementos pesquisados: " + qtdBusca);
	}

	private static void mostraVencedor() {
		Candidato CandRegionalVencedor = abCandidato.toList().stream()
				.filter(c -> c.getTipoCandidato() == TipoCandidato.REGIONAL)
				.max((c1, c2) -> Long.compare(c1.getQtdVotos(), c2.getQtdVotos())).get();

		Candidato CandFederalVencedor = abCandidato.toList().stream()
				.filter(c -> c.getTipoCandidato() == TipoCandidato.FEDERAL)
				.max((c1, c2) -> Long.compare(c1.getQtdVotos(), c2.getQtdVotos())).get();

		System.out.println("-------Candidatos vencedores!--------");
		System.out.println("<<<<<Candidato Regional>>>>>");
		System.out.println(CandRegionalVencedor.getNome() + "\t" + CandRegionalVencedor.getPartido().name());
		System.out.println("Quantidade de votos: " + CandRegionalVencedor.getQtdVotos());
		System.out.println("\n<<<<<Candidato Federal>>>>>");
		System.out.println(CandFederalVencedor.getNome() + "\t" + CandFederalVencedor.getPartido().name());
		System.out.println("Quantidade de votos: " + CandFederalVencedor.getQtdVotos());
	}

}
