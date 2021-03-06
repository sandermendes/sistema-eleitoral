package pi.node;

import pi.model.Candidato;
import pi.model.Eleitor;
import pi.model.Elemento;

/**
 * Classe Node da �rvore bin�ria
 * 
 * @author Jo�o P. Amo�do
 *
 */
@SuppressWarnings("hiding")
public class Node<t extends Elemento> {

	public Node<t> direita;

	public Node<t> esquerda;

	public Node<t> pai = null;

	private Cor cor;

	private t conteudo;

	private int altura;

	private int fat;

	/**
	 * Construtor do Node, recebe um inteiro
	 * 
	 * @param elemento
	 */
	public Node(t conteudo) {
		this.conteudo = conteudo;
		this.direita = null;
		this.esquerda = null;
		this.cor = Cor.RUBRO;
	}

	public Node(t conteudo, Cor cor) {
		this.conteudo = conteudo;
		this.direita = null;
		this.esquerda = null;
		this.cor = cor;
	}

	/**
	 * Retorna o Node a direita
	 * 
	 * @return direita
	 */

	public Node<t> getDireita() {
		return this.direita;
	}

	/**
	 * Retorna o Node a esquerda
	 * 
	 * @return esquerda
	 */
	public Node<t> getEsquerda() {
		return this.esquerda;
	}

	/**
	 * Retorna o elemento do node
	 * 
	 * @return elemento
	 */
	@SuppressWarnings("unchecked")
	public long getElemento() {
		return this.conteudo.getElemento();
	}

	/**
	 * Recebe como par�metro um Node e atribui a sua direita
	 * 
	 * @param direita
	 */
	public void setDireita(Node<t> direita) {
		this.direita = direita;
	}

	/**
	 * Define o fator de balanceament do Node
	 */
	public void setFat() {
		int qtd = this.getQuantidadeDeFilhos();

		if (qtd == 0) {
			fat = 0;
		} else if (qtd == 1) {
			fat = -direita.getAltura();
		} else if (qtd == -1) {
			fat = esquerda.getAltura();
		} else {
			fat = esquerda.getAltura() - direita.getAltura();
		}
	}

	/**
	 * Define a altura e o fator de balanceamento
	 */
	public void setAlturaEFat() {
		this.setAltura();
		this.setFat();
	}

	/**
	 * Define a altura do Node
	 */

	public void setAltura() {
		int qtd = this.getQuantidadeDeFilhos();

		if (qtd == 0)
			altura = 1;
		else if (qtd == 1)
			altura = direita.getAltura() + 1;
		else if (qtd == -1)
			altura = esquerda.getAltura() + 1;
		else {
			if (direita.getAltura() > esquerda.getAltura())
				altura = direita.getAltura() + 1;
			else
				altura = esquerda.getAltura() + 1;
		}

	}

	/**
	 * Retorna o conteudo do Node
	 * @return
	 */
	public t getConteudo() {
		return (t) this.conteudo;
	}

	/**
	 * Retorna a altura do Node na �rvore
	 * @return
	 */
	public int getAltura() {
		return this.altura;
	}

	/**
	 * Recebe como par�metro um Node e atribui a sua esquerda
	 * 
	 * @param esquerda
	 */
	public void setEsquerda(Node<t> esquerda) {
		this.esquerda = esquerda;
	}

	/**
	 * Retorna 2 caso os dois filhos sejam diferentes de nulo Retorna 1 caso apenas
	 * o filho da direita seja diferente de nulo Retorna -1 caso apenas o filho da
	 * esquerda seja diferente de nulo Retorna 0 caso os dois filhos sejam nulos
	 * 
	 * @return qtdFilhos
	 */
	public int getQuantidadeDeFilhos() {
		if (direita == null && esquerda == null) {
			return 0;
		} else if (direita != null && esquerda == null) {
			return 1;
		} else if (direita == null && esquerda != null) {
			return -1;
		} else {
			if (direita.getConteudo() == null && esquerda.getConteudo() == null)
				return 0;
			else if (esquerda.getConteudo() == null && direita.getConteudo() != null)
				return 1;
			else if (direita.getConteudo() == null && esquerda.getConteudo() != null)
				return -1;
			else
				return 2;
		}
	}

	/**
	 * Retorna a cor do Node
	 * @return cor
	 */
	public Cor getCor() {
		return cor;
	}

	/**
	 * Atribui uma cor ao Node
	 * @param cor
	 */
	public void setCor(Cor cor) {
		this.cor = cor;
	}

	@Override
	public String toString() {
		return this.conteudo + "";
	}

	/**
	 * Atribui o Node pai ao Node raiz
	 * @param pai
	 */
	public void setPai(Node<t> pai) {
		this.pai = pai;

	}

	/**
	 * Retorna o pai do Node
	 * @return
	 */
	public Node<t> getPai() {

		return pai;
	}

	/**
	 * Retorna o fator de balanceamento
	 * @return
	 */
	public int getFat() {
		return this.fat;
	}

}
