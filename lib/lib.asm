
SECTION .text 

_g__Zstrcatss:
        push    r15
        push    r14
        push    r13
        mov     r13, rdi
        push    r12
        push    rbp
        mov     rbp, rsi
        push    rbx
        sub     rsp, 8
        mov     r14, qword [rdi]
        mov     r12, qword [rsi]
        lea     r15, [r14+r12]
        lea     rdi, [r15+9H]
        call    malloc
        lea     rsi, [r13+8H]
        mov     rdx, r14
        mov     qword [rax], r15
        mov     rbx, rax
        lea     rdi, [rax+8H]
        call    memcpy
        lea     rdi, [rbx+r14+8H]
        lea     rdx, [r12+1H]
        lea     rsi, [rbp+8H]
        call    memcpy
        add     rsp, 8
        mov     rax, rbx
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        pop     r15
        ret


ALIGN   16

_g__Zstrcmpss:
        mov     eax, 1
        mov     rcx, qword [rsi]
        cmp     qword [rdi], rcx
        jg      L_001
        mov     rax, -1
        jl      L_001
        sub     rsp, 8
        add     rsi, 8
        add     rdi, 8
        call    strcmp
        xor     edx, edx
        test    eax, eax
        setne   dl
        neg     rdx
        test    eax, eax
        mov     eax, 1
        cmovle  rax, rdx
        add     rsp, 8
        ret

ALIGN   8
L_001:  ret


ALIGN   8

_g__Zstring4ordi:
        movsx   rax, byte [rdi+rsi+8H]
        ret

ALIGN   16

_g__Zstring4substringii:
  		push    r13
        mov     r13, rdi
        push    r12
        movsxd  r12, esi
        push    rbp
        sub     edx, r12d
        push    rbx
        lea     ebx, [rdx+1H]
        add     edx, 10
        movsxd  rdi, edx
        movsxd  rbx, ebx
        sub     rsp, 8
        call    malloc
        lea     rsi, [r13+r12+8H]
        mov     rdx, rbx
        mov     qword [rax], rbx
        mov     rbp, rax
        lea     rdi, [rax+8H]
        call    memcpy
        mov     byte [rbp+rbx+8H], 0
        add     rsp, 8
        mov     rax, rbp
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        ret

ALIGN   8

_g__ZgetString:
        push    rbp
        mov     edi, 264
        push    rbx
        sub     rsp, 8
        call    malloc
        lea     rdi, [rel LC0]
        lea     rbp, [rax+8H]
        mov     rbx, rax
        xor     eax, eax
        mov     rsi, rbp
        call    __isoc99_scanf
        mov     rdx, rbp
L_002:  mov     ecx, dword [rdx]
        add     rdx, 4
        lea     eax, [rcx-1010101H]
        not     ecx
        and     eax, ecx
        and     eax, 80808080H
        jz      L_002
        mov     ecx, eax
        shr     ecx, 16
        test    eax, 8080H
        cmove   eax, ecx
        lea     rcx, [rdx+2H]
        cmove   rdx, rcx
        mov     esi, eax
        add     sil, al
        mov     rax, rbx
        sbb     rdx, 3
        sub     rdx, rbp
        mov     qword [rbx], rdx
        add     rsp, 8
        pop     rbx
        pop     rbp
        ret


_g__ZgetInt:
        sub     rsp, 24
        lea     rdi, [rel LC1]
        xor     eax, eax
        lea     rsi, [rsp+8H]
        call    __isoc99_scanf
        mov     rax, qword [rsp+8H]
        add     rsp, 24
        ret


ALIGN   16

_g__Zstring4parseInt:
        mov     rax, qword [rdi]
        test    rax, rax
        jle     L_005
        movsx   edx, byte [rdi+8H]
        lea     ecx, [rdx-30H]
        cmp     cl, 9
        ja      L_005
        lea     rcx, [rdi+9H]
        lea     rdi, [rdi+rax+8H]
        xor     eax, eax
        jmp     L_004





ALIGN   8
L_003:  movsx   edx, byte [rcx]
        add     rcx, 1
        lea     esi, [rdx-30H]
        cmp     sil, 9
        ja      L_006
L_004:  sub     edx, 48
        lea     rax, [rax+rax*4]
        movsxd  rdx, edx
        lea     rax, [rdx+rax*2]
        cmp     rdi, rcx
        jnz     L_003
        ret

ALIGN   8
L_005:  xor     eax, eax
L_006:  ret


ALIGN   16

_g__ZprintInti:
        mov     rsi, rdi
        xor     eax, eax
        lea     rdi, [rel LC1]
        jmp     printf

ALIGN   16

_g__ZprintIntlni:
        mov     rsi, rdi
        xor     eax, eax
        lea     rdi, [rel LC2]
        jmp     printf



ALIGN   16

_g__Zprintlns:
        add     rdi, 8
        jmp     puts

ALIGN   8

_g__Zprints:
        lea     rsi, [rdi+8H]
        xor     eax, eax
        lea     rdi, [rel LC0]
        jmp     printf


ALIGN   16

_g__ZtoStringi:
        push    rbx
        mov     rbx, rdi
        mov     edi, 30
        call    malloc
        xor     r10d, r10d
        mov     rsi, rax
        lea     rdi, [rax+8H]
        test    rbx, rbx
        jns     L_007
        neg     rbx
        mov     r10d, 1
L_007:  mov     r9, qword 0CCCCCCCCCCCCCCCDH
        xor     ecx, ecx




ALIGN   8
L_008:  mov     rax, rbx
        movsx   r8, cx
        mul     r9
        shr     rdx, 3
        lea     rax, [rdx+rdx*4]
        add     rax, rax
        sub     rbx, rax
        mov     eax, ecx
        add     ecx, 1
        add     ebx, 48
        mov     byte [rdi+r8+8H], bl
        mov     rbx, rdx
        test    rdx, rdx
        jnz     L_008
        movsx   r9, cx
        lea     r11, [rdi+r9+8H]
        test    r10b, r10b
        jz      L_009
        lea     ecx, [rax+2H]
        mov     byte [r11], 45
        movsx   r9, cx
        lea     r11, [rdi+r9+8H]
L_009:  movsx   ecx, cx
        xor     eax, eax
        sub     ecx, 1
        movsxd  rdx, ecx
        test    ecx, ecx
        jle     L_011




ALIGN   8
L_010:  movzx   ecx, byte [rsi+rax+10H]
        movzx   r8d, byte [rsi+rdx+10H]
        mov     byte [rsi+rax+10H], r8b
        add     rax, 1
        mov     byte [rsi+rdx+10H], cl
        sub     rdx, 1
        cmp     edx, eax
        jg      L_010
L_011:  mov     byte [r11], 0
        mov     rax, rdi
        mov     qword [rsi+8H], r9
        pop     rbx
        ret


SECTION .data   


SECTION .bss    


SECTION .rodata.str1.1 

LC0:
        db 25H, 73H, 00H

LC1:
        db 25H, 6CH, 6CH, 64H, 00H

LC2:
        db 25H, 6CH, 6CH, 64H, 0AH, 00H