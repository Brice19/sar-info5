Conversation ouverte. 1 message lu.

Aller au contenu
Utiliser Gmail avec un lecteur d'écran
		4 sur 3 904
		[etu-2024-polytech-kai5in210] Polytech - Info5 - SAR - Updated Circular Buffer
Boîte de réception

Olivier Gruber
Pièces jointes
09:49 (il y a 4 heures)
À etu-2024-polytech-kai5in210

Chers étudiants,

Il y a une nouvelle version sur Moodle du circular buffer, elle est en
pièce jointe aussi.

La différence? Le mot clé "volatile" sur les champs. Je ne suis pas sûr
de pourquoi il s'était perdu...

Désolé si cela a été une source de bug pour vous.

Avec un echo server, avec 40 clients, 20 connections successives pour
		chaque client, et des messages de 1 byte jusqu'à 10KB, et bien...
surprise... Ce code marchait avec 2 coeurs, 4 hardware threads, et la
version 1.8 de Java sur un Ubuntu 20.04... Tout en étant incorrect car
le mot clé "volatile" se devait d'être présent. Mais sur un  8 coeurs,
		16 hardware threads, Java-11, et Ubuntu 24.04... cela ne passe plus.
Qu'est-ce qui a changé de comportement? Le compilo? La JVM? Le scheduler
de Ubuntu? Le hardware avec plus de "vrai parallélisme"? Impossible de
savoir.

		C'est un très bel exemple de ce que nous discutions en cours sur
l'importance d'une conception et d'un codage correct, avec une
connaissance experte du langage utilisé, car passer des tests ne dit
malheureusement plus grand chose sur la correction d'un code parallèle
et encore moins sur celle d'un code distribué.

A demain.

		--
Regards,
Pr. Olivier Gruber
 1 pièce jointe
  • Analyse effectuée par Gmail
/*
 * Copyright (C) 2023 Pr. Olivier Gruber
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info5.sar.utils;

/**
 * This circular buffer of bytes can be used to pass bytes between two threads:
 * one thread pushing bytes in the buffer and the other pulling bytes from the
 * buffer. The buffer policy is FIFO: first byte in is the first byte out.
 */
public class CircularBuffer {
	volatile int m_tail, m_head;
	volatile byte m_bytes[];

	public CircularBuffer(int capacity) {
		m_bytes = new byte[capacity];
		m_tail = m_head = 0;
	}

	/**
	 * @return true if this buffer is full, false otherwise
	 */
	public boolean full() {
		int next = (m_head + 1) % m_bytes.length;
		return (next == m_tail);
	}

	/**
	 * @return true if this buffer is empty, false otherwise
	 */
	public boolean empty() {
		return (m_tail == m_head);
	}

	/**
	 * @param b: the byte to push in the buffer
	 * @return the next available byte
	 * @throws an IllegalStateException if full.
	 */
	public void push(byte b) {
		int next = (m_head + 1) % m_bytes.length;
		if (next == m_tail)
			throw new IllegalStateException();
		m_bytes[m_head] = b;
		m_head = next;
	}

	/**
	 * @return the next available byte
	 * @throws an IllegalStateException if empty.
	 */
	public byte pull() {
		if (m_tail == m_head)
			throw new IllegalStateException();
		int next = (m_tail + 1) % m_bytes.length;
		byte bits = m_bytes[m_tail];
		m_tail = next;
		return bits;
	}

}
CircularBuffer.java
Affichage de CircularBuffer.java en cours...