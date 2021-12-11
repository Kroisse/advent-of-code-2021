use std::io;

use ndarray::{prelude::*, Array2};

pub fn solve(reader: &mut dyn io::BufRead) -> Result<(), crate::Error> {
    let mut grid = read(reader)?;

    let flashes = (0..100).map(|_| step(&mut grid)).sum::<usize>();

    println!("Octopus flashes: {}", flashes);

    Ok(())
}

fn read(reader: &mut dyn io::BufRead) -> Result<Array2<u8>, crate::Error> {
    let mut line = String::new();
    if reader.read_line(&mut line)? <= 0 {
        return Err(io::Error::new(io::ErrorKind::UnexpectedEof, "Empty input").into());
    }
    let input = line.trim();
    let mut grid = Array2::from_elem((input.len() + 2, input.len() + 2), 255u8);
    read_row(&mut grid.slice_mut(s![1, 1..-1]), input);

    for row in 2..=input.len() {
        line.clear();
        if reader.read_line(&mut line)? <= 0 {
            break;
        }
        read_row(&mut grid.slice_mut(s![row, 1..-1]), line.trim());
    }
    Ok(grid)
}

fn read_row(row: &mut ArrayViewMut1<'_, u8>, input: &str) {
    for (col, ch) in input.chars().take(row.len()).enumerate() {
        row[col] = (ch.to_digit(10).unwrap_or(0)) as u8;
    }
}

fn step(grid: &mut Array2<u8>) -> usize {
    let mut flashes = vec![];
    let mut flashes_tail = 0;

    for (ix, cell) in grid.indexed_iter_mut() {
        *cell = cell.saturating_add(1);
        if *cell != 255 && *cell >= 10 {
            flashes.push(ix);
        }
    }
    while flashes_tail < flashes.len() {
        let range = flashes_tail..flashes.len();
        flashes_tail = flashes.len();

        for i in range {
            let (row, col) = flashes[i];

            let mut neighbors = grid.slice_mut(s![row - 1..=row + 1, col - 1..=col + 1]);
            for ((dr, dc), cell) in neighbors.indexed_iter_mut() {
                *cell = cell.saturating_add(1);
                if *cell != 255 && *cell == 10 {
                    flashes.push((row + dr - 1, col + dc - 1));
                }
            }
        }
    }

    for ix in &flashes {
        grid[*ix] = 0;
    }

    flashes.len()
}

#[cfg(test)]
mod test {
    use super::*;

    const G: u8 = 255;

    #[test]
    fn basic_example() {
        let mut grid = array![
            [G, G, G, G, G, G, G],
            [G, 1, 1, 1, 1, 1, G],
            [G, 1, 9, 9, 9, 1, G],
            [G, 1, 9, 1, 9, 1, G],
            [G, 1, 9, 9, 9, 1, G],
            [G, 1, 1, 1, 1, 1, G],
            [G, G, G, G, G, G, G],
        ];
        let grid2 = array![
            [G, G, G, G, G, G, G],
            [G, 3, 4, 5, 4, 3, G],
            [G, 4, 0, 0, 0, 4, G],
            [G, 5, 0, 0, 0, 5, G],
            [G, 4, 0, 0, 0, 4, G],
            [G, 3, 4, 5, 4, 3, G],
            [G, G, G, G, G, G, G],
        ];
        let grid3 = array![
            [G, G, G, G, G, G, G],
            [G, 4, 5, 6, 5, 4, G],
            [G, 5, 1, 1, 1, 5, G],
            [G, 6, 1, 1, 1, 6, G],
            [G, 5, 1, 1, 1, 5, G],
            [G, 4, 5, 6, 5, 4, G],
            [G, G, G, G, G, G, G],
        ];
        let flashes = step(&mut grid);
        assert_eq!(flashes, 9);
        assert_eq!(grid, grid2);
        step(&mut grid);
        assert_eq!(grid, grid3);
    }
}
