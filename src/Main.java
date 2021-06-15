import model.BlockChain;
import java.util.Scanner;
import java.util.stream.IntStream;

public final class Main {
    public static void main(final String[] args) {
        final var sc = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        final var hashZeroes = sc.nextInt();
        final var blockChain = new BlockChain(hashZeroes);
        blockChain.load();

        IntStream.range(0, 5).forEach(block -> blockChain.addBlock());

        System.out.println(blockChain);
        blockChain.save();
    }
}
