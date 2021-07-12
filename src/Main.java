import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import model.BlockChain;
import model.Miner;

public final class Main {
    public static void main(final String[] args) throws InterruptedException {
        final var sc = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        final var hashZeroes = sc.nextInt();
        final var blockChain = new BlockChain(hashZeroes);
        blockChain.load();

        final List<Miner> miners = new ArrayList<>();
        IntStream.range(0, 5).forEach(minerId -> miners.add(new Miner(minerId, blockChain)));
        miners.forEach(Thread::start);

        for (final Miner miner : miners) {
            miner.join();
        }

        System.out.println(blockChain);
        blockChain.save();
    }
}
