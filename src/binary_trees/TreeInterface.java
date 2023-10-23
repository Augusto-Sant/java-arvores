package binary_trees;

public interface TreeInterface {
    /**
     * mostra a arvore
     */
    void show();

    /**
     * insere o valor passado ou o valor correspondente a chave passada.
     * @param data ou key no caso da Btree
     */
    void insert(int data);

    /**
     * remove o valor passado ou o valor correspondente a chave passada.
     * @param data ou key no caso da Btree
     */
    void remove(int data);

    /**
     * Procura o valor passado ou o valor correspondente a chave passada.
     * @param data ou key no caso da Btree
     */
   int get(int data);
}
