package pi.arvore;
import pi.model.Elemento;
import pi.node.Node;


public abstract class ArvoreBalanceada<T extends Elemento> extends ArvoreBinariaDeBusca<T>{
	
	public ArvoreBalanceada(T elemento) {
		super(elemento);
	}
	
	public ArvoreBalanceada() {
		super();
		
	}
	protected Node<T> RSE(Node<T> raiz) {
		
		Node<T> aux = raiz.getDireita();
		raiz.setDireita(aux.getEsquerda());
		aux.setEsquerda(raiz);
		return aux;
	}
	
	
	protected Node<T> RSD(Node<T> raiz) {
		Node<T> aux = raiz.getEsquerda();
		raiz.setEsquerda(aux.getDireita());	
		aux.setDireita(raiz);		
		return aux;

	}
	
	protected Node<T> RDE(Node<T> raiz) {
		raiz.setDireita(RSD(raiz.getDireita()));
		Node<T> rse = RSE(raiz);
		return rse;
		
	}
	
	protected Node<T> RDD(Node<T> raiz) {
		raiz.setEsquerda(RSE(raiz.getEsquerda()));
		return RSD(raiz);
	}

}
