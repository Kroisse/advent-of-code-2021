use std::io;

fn main() -> io::Result<()> {
    let stdin = io::stdin();
    let mut line = String::new();
    let mut input = vec![];
    loop {
        line.clear();
        if stdin.read_line(&mut line)? == 0 {
            break;
        }
        if let Ok(n) = line.trim().parse::<i32>() {
            input.push(n);
        }
    }

    let mut count = 0;
    input.iter().reduce(|prev, current| {
        if prev < current {
            count += 1
        }
        current
    });
    println!("Part 1: {}", count);

    count = 0;
    windowed_3(&input)
        .map(|(a, b, c)| a + b + c)
        .reduce(|prev, current| {
            if prev < current {
                count += 1
            }
            current
        });
    println!("Part 2: {}", count);

    Ok(())
}

fn windowed_3<T>(iterable: impl IntoIterator<Item = T>) -> impl Iterator<Item = (T, T, T)>
where
    T: Copy,
{
    iterable
        .into_iter()
        .scan(None, |state, n| match *state {
            None => {
                *state = Some((None, n));
                Some(None)
            }
            Some((None, b)) => {
                *state = Some((Some(b), n));
                Some(None)
            }
            Some((Some(a), b)) => {
                *state = Some((Some(b), n));
                Some(Some((a, b, n)))
            }
        })
        .filter_map(|i| i)
}
